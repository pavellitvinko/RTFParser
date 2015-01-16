package parser;

import java.util.HashMap;
import java.util.Map;

public class HTMLTag {
	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	private String name;
	private Map<String, String> params = new HashMap<String, String>();
	
	public HTMLTag(String name) {
		this.name = name;
	}
	
	public HTMLTag addParam(String name, String value) {
		params.put(name, value);
		return this;
	}
	
	public String toHTMLCode() {
		return toHTMLCode(true);
	}
	
	public String toHTMLCode(boolean modifier) {
		StringBuilder HTMLTag = new StringBuilder();
		if (modifier == true) {
			HTMLTag.append("<");
		} else {
			HTMLTag.append("</");
		}
		HTMLTag.append(name);
		
		if (modifier == true && !params.isEmpty()) {
			for (String key : params.keySet()) {
				HTMLTag.append(String.format(" %s=\"%s\"",key,params.get(key)));
			}
		}
		HTMLTag.append(">");
		return HTMLTag.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
