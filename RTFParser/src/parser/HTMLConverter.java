/**
 * 
 */
package parser;

import java.util.ArrayList;
import java.util.List;

import parser.RTFSpecification.FormattingProperty;

/**
 * @author pavellitvinko
 * 
 */
public class HTMLConverter extends FormatedConverter {
	private StringBuilder currText = new StringBuilder();
	private List<String> result = new ArrayList<String>();
	private HTMLTag FontTag = null;
	private HTMLTag ParagraphTag = null;

	public HTMLConverter() {
		result.add(new HTMLTag(HTMLSpecification.TagIdHTML).toHTMLCode());
		result.add(new HTMLTag(HTMLSpecification.TagIdBody).toHTMLCode());
	}

	@Override
	protected void addText(String text) {
		super.addText(text);
		currText.append(text);
	};

	@Override
	protected void addParagraph() {
		super.addParagraph();
		currText.append(new HTMLTag(HTMLSpecification.TagIdLineBreak)
				.toHTMLCode(true));
		flushText();
	}

	@Override
	protected void doChangeFormatting(FormattingProperty prop, boolean modifier) {
		super.doChangeFormatting(prop, modifier);
		switch (prop) {
		case Bold:
			currText.append(new HTMLTag(HTMLSpecification.TagIdBold)
					.toHTMLCode(modifier));
			break;
		case Italic:
			currText.append(new HTMLTag(HTMLSpecification.TagIdItalic)
					.toHTMLCode(modifier));
			break;
		case Strikethrough:
			currText.append(new HTMLTag(HTMLSpecification.TagIdStrikethrough)
					.toHTMLCode(modifier));
			break;
		case Underline:
			currText.append(new HTMLTag(HTMLSpecification.TagIdUnderline)
					.toHTMLCode(modifier));
			break;
		default:
			break;
		}
	}

	@Override
	protected void doChangeFont(RTFFontInfo newFont) {
		super.doChangeFont(newFont);
		closeFontTags();
		FontTag = new HTMLTag(HTMLSpecification.TagIdFont).addParam(
				HTMLSpecification.TagParamFace, newFont.getName());
		result.add(FontTag.toHTMLCode());
	}

	@Override
	protected void doChangeFontSize(int newSize) {
		super.doChangeFontSize(newSize);
		closeParagraphTags();
		ParagraphTag = new HTMLTag(HTMLSpecification.TagIdParagraph);
		ParagraphTag.addParam(HTMLSpecification.TagParamStyle, String.format(
				"%s:%dpx", HTMLSpecification.TagParamFontSize, newSize/2));
		result.add(ParagraphTag.toHTMLCode());
	}


	@Override
	protected void doChangeColor(RTFColor newColor,
			RTFColor.DisplayType colorType) {
		super.doChangeColor(newColor, colorType);
	}

	@Override
	public List<String> getResult() {
		flushText();
		closeFontTags();
		closeParagraphTags();
		result.add(new HTMLTag(HTMLSpecification.TagIdBody).toHTMLCode(false));
		result.add(new HTMLTag(HTMLSpecification.TagIdHTML).toHTMLCode(false));
		return result;
	}

	private void closeFontTags() {
		if (FontTag != null) {
			result.add(FontTag.toHTMLCode(false));
			FontTag = null;
		}
	}
	private void closeParagraphTags() {
		if (ParagraphTag != null) {
			result.add(ParagraphTag.toHTMLCode(false));
			ParagraphTag = null;
		}
	}

	private void flushText() {
		result.add(currText.toString());
		currText.setLength(0);
	}

}
