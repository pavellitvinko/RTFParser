package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavellitvinko
 *
 */
public class TextConverter extends RTFConverter {
	private StringBuilder currText = new StringBuilder();
	private List<String> result = new ArrayList<String>();
	
	@Override
	protected void addText(String text) {
		super.addText(text);
		currText.append(text);
	};
	
	@Override
	protected void addParagraph(){
		super.addParagraph();
		flushText();
	}
	
	@Override
	public List<String> getResult(){
		flushText();
		return result;
	}
	
	private void flushText(){
		result.add(currText.toString());
		currText.setLength(0);
	}
}
