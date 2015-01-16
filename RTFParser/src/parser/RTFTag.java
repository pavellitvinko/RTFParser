package parser;

public class RTFTag {
	private String name;
	private String value;

	public RTFTag(String name) {
		this.name = name;
		this.value = "";
	}

	public RTFTag(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public int getIntValue() {
		int result;
		try {
			result = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			result = -1;
		}
		return result;
	}
}
