package parser;

import java.util.HashMap;
import java.util.Map;

import parser.RTFSpecification.FormattingProperty;

/**
 * @author pavellitvinko
 * 
 */
public class FormatedConverter extends RTFConverter {
	protected Map<Integer, RTFFontInfo> fontTable = new HashMap<Integer, RTFFontInfo>();
	protected Map<Integer, RTFColor> colorTable = new HashMap<Integer, RTFColor>();
	private int fontTableStartLevel = -1;
	private int targetFontId = -1;
	private int targetColorId = -1;
	private RTFFontInfo targetFont = null;
	private RTFFontInfo activeFont = null;
	private RTFColor targetColor = null;
	private RTFColor activeColor = null;
	private boolean expectingColorDefinition = false;

	public FormatedConverter() {
		// Define default color
		targetColor = new RTFColor(255, 255, 255);
		addTargetColorToTable();
	}

	@Override
	protected void addTag(RTFTag tag) {
		super.addTag(tag);

		String tagName = tag.getName();
		switch (tagName) {

		case RTFSpecification.TagFontTable:
			// Remember we're in the font-table definition
			fontTableStartLevel = super.getLevel();
			break;

		case RTFSpecification.TagColorTable:
			expectingColorDefinition = true;
			break;

		case RTFSpecification.TagFont:
			if (fontTableStartLevel > 0) {
				targetFontId = tag.getIntValue();
				targetFont = new RTFFontInfo();
			} else if (!fontTable.isEmpty()) {
				activeFont = fontTable.get(tag.getIntValue());
				this.doChangeFont(activeFont);
			}
			break;

		case RTFSpecification.TagFontSize:
			doChangeFontSize(tag.getIntValue());
			break;

		case RTFSpecification.TagColorForeground:
			activeColor = colorTable.get(tag.getIntValue());
			doChangeColor(activeColor, RTFColor.DisplayType.ForegroundColor);
			break;

		case RTFSpecification.TagColorBackground:
			activeColor = colorTable.get(tag.getIntValue());
			doChangeColor(activeColor, RTFColor.DisplayType.BackgroundColor);
			break;

		}

		parseFormattingTags(tag, tagName);

		// Font table definition
		if (fontTableStartLevel > 0 && targetFont != null) {
			switch (tagName) {
			case RTFSpecification.TagFontFamilyDefault:
			case RTFSpecification.TagFontFamilyRoman:
			case RTFSpecification.TagFontFamilySwiss:
			case RTFSpecification.TagFontFamilyModern:
			case RTFSpecification.TagFontFamilyScript:
			case RTFSpecification.TagFontFamilyDecorative:
			case RTFSpecification.TagFontFamilySymbol:
			case RTFSpecification.TagFontFamilyBidirectional:
				targetFont.setFamily(tagName);
				break;

			case RTFSpecification.TagFontCharset:
				targetFont.setCodePage(tag.getIntValue());
				break;

			case RTFSpecification.TagFontName:
				targetFont.setName(tag.getValue());
				addTargetFontToTable();
				break;
			}
		}
		// Color table definition
		if (expectingColorDefinition) {
			switch (tagName) {
			case RTFSpecification.TagColorRed:
				if (targetColor == null)
					targetColor = new RTFColor();
				targetColor.setRed(tag.getIntValue());
				break;
			case RTFSpecification.TagColorGreen:
				targetColor.setGreen(tag.getIntValue());
				break;
			case RTFSpecification.TagColorBlue:
				targetColor.setBlue(tag.getIntValue());
				break;
			}

		}
	}



	@Override
	protected void doEndGroup() {
		if (fontTableStartLevel == super.getLevel()) {
			fontTableStartLevel = -1;
			targetFontId = -1;
			targetFont = null;
		}
		if (expectingColorDefinition) {
			addTargetColorToTable();
			expectingColorDefinition = false;
		}
		super.doEndGroup();
	};

	@Override
	protected void doDelimit() {
		super.doDelimit();
		if (expectingColorDefinition) {
			addTargetColorToTable();
		}
	}

	private void addTargetFontToTable() {
		if (targetFont != null) {
			fontTable.put(targetFontId, targetFont);
			doDefineFont(targetFontId, targetFont);
			targetFont = null;
		}
	}

	private void addTargetColorToTable() {
		if (targetColor != null) {
			colorTable.put(++targetColorId, targetColor);
			doDefineColor(targetColorId, targetColor);
			targetColor = null;
		}
	}

	private void parseFormattingTags(RTFTag tag, String tagName) {
		switch (tagName) {
		
		case RTFSpecification.TagFontFormattingBold:
			if (tag.getIntValue() != 0) {
				// Turn on bold
				doChangeFormatting(FormattingProperty.Bold, true);
			} else
				// Turn off bold
				doChangeFormatting(FormattingProperty.Bold, false);
			break;

		case RTFSpecification.TagFontFormattingItalic:
			if (tag.getIntValue() != 0) {
				// Turn on italic
				doChangeFormatting(FormattingProperty.Italic, true);
			} else
				// Turn off italic
				doChangeFormatting(FormattingProperty.Italic, false);
			break;

		case RTFSpecification.TagFontFormattingUnderline:
			if (tag.getIntValue() != 0) {
				// Turn on underline
				doChangeFormatting(FormattingProperty.Underline, true);
			} else
				// Turn off underline
				doChangeFormatting(FormattingProperty.Underline, false);
			break;
			
		case RTFSpecification.TagFontFormattingUnderlineOff:
			// Stops all underlining
			doChangeFormatting(FormattingProperty.Underline, false);
			break;

		case RTFSpecification.TagFontFormattingStrikethrough:
			if (tag.getIntValue() != 0) {
				// Turn on italic
				doChangeFormatting(FormattingProperty.Strikethrough, true);
			} else
				// Turn off italic
				doChangeFormatting(FormattingProperty.Strikethrough, false);
			break;
		}
	};
	
	protected void doChangeFormatting(FormattingProperty prop, boolean modifier) {
		// Nothing to do
		System.out.printf("Change formatting option %s: %b\n", prop.toString(),
				modifier);
	}

	protected void doDefineFont(int index, RTFFontInfo newFont) {
		// Nothing to do
		System.out.printf("Define a new font %d: \"%s\"\n", index,
				newFont.getName());
	}

	protected void doChangeFont(RTFFontInfo newFont) {
		// Nothing to do
		System.out.printf("Change font to \"%s\"\n", newFont.getName());
	}

	protected void doChangeFontSize(int newSize) {
		// Nothing to do
		System.out.printf("Change font size to %d\n", newSize);

	}

	protected void doDefineColor(int index, RTFColor newColor) {
		// Nothing to do
		System.out.printf("Define a new color %d: (%d,%d,%d)\n", index,
				newColor.getRed(), newColor.getGreen(), newColor.getBlue());
	}

	protected void doChangeColor(RTFColor newColor,
			RTFColor.DisplayType colorType) {
		// Nothing to do
		System.out.printf("Change %s to (%d,%d,%d)\n", colorType
				.toString().toLowerCase(), newColor.getRed(), newColor
				.getGreen(), newColor.getBlue());
	}

}
