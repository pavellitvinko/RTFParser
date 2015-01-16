package parser;

import java.util.List;

/**
 * @author pavellitvinko
 * 
 */
public class RTFConverter {
	private int level;
	
	protected void  addTag(RTFTag tag) {
    	if (tag.getValue().isEmpty())
    		System.out.printf("Found tag: %s\n", tag.getName());
    	else
    		System.out.printf("Found tag: %s:%s\n", tag.getName(), tag.getValue());		
	};

	protected void addText(String text) {
		System.out.printf("Add text: %s\n", text);
	};
	
	protected void addParagraph(){
		System.out.println("Add paragraph");
	}

	protected void addTabulator(){
		System.out.println("--- tabulator ---");
	}
	
	protected void doBeginGroup() {
		level++;
		System.out.println("Begin group");
	};

	protected void doEndGroup() {
		level--;
		System.out.println("End group");
	};
	
	protected void doDelimit() {
		System.out.println("--- delimiter ---");
	}
	
	public List<String> getResult(){
		// Nothing to return because we're not storing result
		return null;
	}

	public int getLevel() {
		return level;
	}
	
}
