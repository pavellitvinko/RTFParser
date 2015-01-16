package parser;

/**
 * Rich Text Format (RTF) Specification, Version 1.9.1
 * @author pavellitvinko
 */
public final class RTFSpecification {
	
	public enum FormattingProperty {
		Bold, Italic, Underline, Strikethrough
	}
	public static final String TagUnicodeSkipCount = "uc";
	public static final String TagUnicodeCode = "u";
	
	// --------- font ---------------------------------------------------
	public static final String TagFontTable = "fonttbl";
	public static final String TagDefaultFont = "deff";
	public static final String TagFont = "f";
	public static final String TagFontName = "fontname";
	public static final String TagFontCharset = "fcharset";
	public static final String TagFontSize = "fs";
	
	public static final String TagFontFamilyDefault = "fnil";
	public static final String TagFontFamilyRoman = "froman";
	public static final String TagFontFamilySwiss = "fswiss";
	public static final String TagFontFamilyModern = "fmodern";
	public static final String TagFontFamilyScript = "fscript";
	public static final String TagFontFamilyDecorative = "fdecor";
	public static final String TagFontFamilySymbol = "ftech";
	public static final String TagFontFamilyBidirectional = "fbidi";
	
	public static final String TagFontPitch = "fprq";
	public static final String TagFontPanose = "panose";
	public static final String TagFontNonTaggedName = "fname";
	public static final String TagFontEmbedding = "fontemb";
	public static final String TagFontCpg = "cpg";
	
	public static final String TagFontFormattingBold = "b";
	public static final String TagFontFormattingItalic = "i";
	public static final String TagFontFormattingUnderline = "ul";
	public static final String TagFontFormattingUnderlineOff = "ulnone";
	public static final String TagFontFormattingStrikethrough = "strike";
	
	public static final String TagThemeFontLoMajor = "flomajor";
	public static final String TagThemeFontHiMajor = "fhimajor";
	public static final String TagThemeFontDbMajor = "fdbmajor";
	public static final String TagThemeFontBiMajor = "fbimajor";
	public static final String TagThemeFontLoMinor = "flominor";
	public static final String TagThemeFontHiMinor = "fhiminor";
	public static final String TagThemeFontDbMinor = "fdbminor";
	public static final String TagThemeFontBiMinor = "fbiminor";
	
	// --------- color ---------------------------------------------------
	public static final String TagColorTable = "colortbl";
	public static final String TagColorRed = "red";
	public static final String TagColorBlue = "blue";
	public static final String TagColorGreen = "green";
	public static final String TagColorForeground = "cf";
	public static final String TagColorBackground= "cb";
	
	// --------- segments ------------------------------------------------
	public static final String TagPage = "page";
	public static final String TagSection = "sect";
	public static final String TagParagraph = "par";
	public static final String TagTabulator = "tab";

}
