package parser;

public class RTFColor {
	public enum DisplayType {
		ForegroundColor, BackgroundColor
	}

	private int red;
	private int green;
	private int blue;

	public RTFColor(int red, int green, int blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	public RTFColor() {
		setRed(0);
		setGreen(0);
		setBlue(0);
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

}
