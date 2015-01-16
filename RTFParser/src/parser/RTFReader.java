package parser;

import java.io.IOException;
import java.io.Reader;

import parser.exceptions.ExceptionMessages;
import parser.exceptions.RTFReaderException;
import parser.exceptions.RTFStructureException;

/**
 * Rich Text Format Reader 
 * Performs parsing of the rtf stream and calls appropriate methods of RTFConverter (builder)
 * @author pavellitvinko
 */
public class RTFReader {
	private int level;
	private int tagCountAtLastGroupStart;
	private int tagCount;
	private int fontTableStartLevel;
	private int fontDefStartLevel;
	private boolean expectingThemeFont;
	private boolean expectingFontName;
	private RTFConverter converter;
	private StringBuilder curText;
	
	public RTFReader(RTFConverter converter) {
		this.converter = converter;
	};

	public void parse(Reader reader) throws IOException, RTFReaderException {
		int eof = -1;
		int groupCount = 0;
		boolean IgnoreContentAfterRootGroup = true;
		level = 0;
		tagCountAtLastGroupStart = 0;
		tagCount = 0;
		fontTableStartLevel = -1;
		fontDefStartLevel = -1;
		expectingThemeFont = false;
		expectingFontName = false;
		curText = new StringBuilder();
		char nextChar = peekNextChar(reader);
		while (nextChar != eof) {
			int peekChar = 0;
			boolean peekCharValid = false;

			switch (nextChar) {
			case '\\':
				reader.read();
				char secondChar = peekNextChar(reader);

				switch (secondChar) {
				case '\\':
				case '{':
				case '}':
					curText.append((char) reader.read());
					break;

				case '\n':
				case '\r':
					reader.read();
					handleTag(reader, new RTFTag(RTFSpecification.TagParagraph));
					converter.addParagraph();
					break;

				case '|':
				case '~':
				case '-':
				case '_':
				case ':':
					handleTag(reader, new RTFTag("" + (char) reader.read()));
					break;

				case '*':
				case '\'':
					reader.read();
					break;

				default:
					parseTag(reader);
					break;
				}
				break;

			case '\n':
			case '\r':
				reader.read();
				break;
			case ';':
				converter.doDelimit();
				reader.read();
				break;

			case '\t':
				reader.read();
				converter.addTabulator();
				handleTag(reader, new RTFTag(RTFSpecification.TagTabulator));
				break;

			case '{':
				reader.read();
				flushText();
				converter.doBeginGroup();
				tagCountAtLastGroupStart = tagCount;
				level++;
				break;

			case '}':
				reader.read();
				flushText();
				if (level > 0) {
					if (fontTableStartLevel == level) {
						fontTableStartLevel = -1;
						expectingThemeFont = false;
					}
					if (fontDefStartLevel == level)
						expectingFontName = false;
					level--;
					converter.doEndGroup();
					groupCount++;
				} else {
					throw new RTFStructureException(ExceptionMessages.ToManyBraces);
				}
				break;

			default:
				if (expectingFontName) {
					parseTag(reader);
					break;
				}
				curText.append((char) reader.read());
				break;
			}
			if (level == 0 && IgnoreContentAfterRootGroup) {
				break;
			}
			if (peekCharValid) {
				nextChar = (char) peekChar;
			} else {
				nextChar = peekNextChar(reader);
			}
		}
		flushText();
		reader.close();

		if (level > 0) {
			throw new RTFStructureException(ExceptionMessages.ToFewBraces);
		}
		if (groupCount == 0) {
			throw new RTFStructureException(ExceptionMessages.NoRtfContent);
		}
		curText = null;
	}

	private boolean handleTag(Reader reader, RTFTag tag) throws RTFReaderException {
		String tagName = tag.getName();
		boolean detectTableDef = expectingThemeFont;
		boolean skippedContent = false;

		if (level == 0)
			throw new RTFStructureException(ExceptionMessages.TagOnRootLevel);

		// Fonts Table definition

		if (tagCountAtLastGroupStart == tagCount) {
			// first tag in a group
			switch (tagName) {
			case RTFSpecification.TagThemeFontLoMajor:
			case RTFSpecification.TagThemeFontHiMajor:
			case RTFSpecification.TagThemeFontDbMajor:
			case RTFSpecification.TagThemeFontBiMajor:
			case RTFSpecification.TagThemeFontLoMinor:
			case RTFSpecification.TagThemeFontHiMinor:
			case RTFSpecification.TagThemeFontDbMinor:
			case RTFSpecification.TagThemeFontBiMinor:
				expectingThemeFont = true;
				break;
			}
			detectTableDef = true;
		}

		if (detectTableDef) {
			// first tag in a group:
			switch (tagName) {
			case RTFSpecification.TagFont:
				if (fontTableStartLevel > 0) {
					fontDefStartLevel = level;
					expectingFontName = true;
				}
				break;

			case RTFSpecification.TagFontTable:
				fontTableStartLevel = level;
				expectingThemeFont = true;
				break;

			}	
		}

		if (expectingFontName) {
			try{
			switch (tagName) {
			case RTFSpecification.TagFontFamilyDefault:
			case RTFSpecification.TagFontFamilyRoman:
			case RTFSpecification.TagFontFamilySwiss:
			case RTFSpecification.TagFontFamilyModern:
			case RTFSpecification.TagFontFamilyScript:
			case RTFSpecification.TagFontFamilyDecorative:
			case RTFSpecification.TagFontFamilySymbol:
			case RTFSpecification.TagFontFamilyBidirectional:
			case RTFSpecification.TagFontCharset:
			case RTFSpecification.TagFont:
			case RTFSpecification.TagFontPitch:
			case RTFSpecification.TagFontEmbedding:
			case RTFSpecification.TagFontNonTaggedName:
			case RTFSpecification.TagFontCpg:
				break;

			case RTFSpecification.TagFontPanose:
				reader.read();
				parseDelimitedTag(reader, tag, false);
				skippedContent = true;
				break;

			default:
				if (fontDefStartLevel == level) {
					RTFTag fontNameTag = new RTFTag(RTFSpecification.TagFontName,
							tag.getName());
					parseDelimitedTag(reader, fontNameTag, true);
					expectingFontName = false;
					tag = fontNameTag;
				}
			} }
			catch (IOException e){
				e.addSuppressed(new RTFStructureException(ExceptionMessages.UnexpectedEnd)); 
			}
		}

		switch (tagName) {
		case RTFSpecification.TagUnicodeCode:
		case RTFSpecification.TagUnicodeSkipCount:
			break;
		default:
			flushText();
			converter.addTag(tag);
			break;
		}

		tagCount++;
		return skippedContent;
	}

	private void parseTag(Reader reader) throws RTFReaderException {
		StringBuilder tagName = new StringBuilder();
		StringBuilder tagValue = null;
		boolean readingName = true;
		boolean delimReached = false;
		try {
		char nextChar = peekNextChar(reader);
		while (!delimReached) {
			if (readingName && isASCIILetter(nextChar)) {
					tagName.append((char) reader.read());
				
			} else if (isDigit(nextChar)
					|| (nextChar == '-' && tagValue == null)) {
				readingName = false;
				if (tagValue == null)
					tagValue = new StringBuilder();
				tagValue.append((char) reader.read());
			} else {
				delimReached = true;
				RTFTag newTag;
				if (tagValue != null && tagValue.length() > 0) {
					newTag = new RTFTag(tagName.toString(), tagValue.toString());
				} else {
					newTag = new RTFTag(tagName.toString());
				}
				boolean skippedContent = handleTag(reader, newTag);
				if (nextChar == ' ' && !skippedContent) {
					reader.read();
				}
			}
			if (!delimReached) {
				nextChar = peekNextChar(reader);
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseDelimitedTag(Reader reader, RTFTag tag,
			boolean AppendValue) throws IOException {
		StringBuilder tagValue = new StringBuilder();
		boolean delimReached = false;

		if (AppendValue)
			tagValue.append(tag.getValue());

		char nextChar = peekNextChar(reader);
		while (!delimReached) {
			if (nextChar != ';' && nextChar != '}') {
				tagValue.append((char) reader.read());
			} else {
				delimReached = true;
				tag.setValue(tagValue.toString());
			}
			if (nextChar == ';')
				reader.read();
			if (!delimReached) {
				nextChar = peekNextChar(reader);
			}
		}
	}

	private char peekNextChar(Reader reader) throws IOException {
		reader.mark(1);
		char peekedChar = (char) reader.read();
		reader.reset();
		return peekedChar;
	}


	private void flushText() throws RTFReaderException {
		if (curText.length() > 0) {
			if (level == 0)
				throw new RTFStructureException(ExceptionMessages.TextOnRootLevel);
			converter.addText(curText.toString());
			curText.setLength(0);
		}
	}


	private boolean isDigit(int Char) {
		return Char >= '0' && Char <= '9';
	}

	private boolean isASCIILetter(int character) {
		return (character >= 'a' && character <= 'z')
				|| (character >= 'A' && character <= 'Z');
	}

}
