package util.jsontools;
/**
 * This class is used to handle the reading of a Jason mor comfortably.
 * @author Eddie
 *
 */
public class JsonReader {
	/**
	 * The JSON formated String to be red.
	 */
	private String json;
	/**
	 * The point the Reader is reading in the Stirng.
	 */
	private int head = 0;
/**
 * Initializes the {@code JsonReader} with the given String.
 * @param json A String with JSON Format.
 */
	public JsonReader(String json) {
		this.json = json;
	}
/**
 * Get the char at the head of the Reader
 * @return char at head
 */
	public char getHead() {
		// System.out.print(json.charAt(head+1));
		return json.charAt(head);
	}
/**
 * Get the next char and move the head further.
 * @return the char a Position higher than the head.
 */
	public char getNext() {
		if (head < json.length() - 1) {
			head++;
			while (head < json.length() - 1
					&& (json.charAt(head) == ' ' || json.charAt(head) == '\n' || json.charAt(head) == '\r' || json.charAt(head) == '\t')) {
				head++;
			}
		}
		return json.charAt(head);
	}
	/**
	 * Get the char the head pointed to last.
	 * @return the char one place before the head.
	 */
	public char getPrevious(){
		int c = head;
		if(c>1){
		c--;
		while (c > 0
				&& (json.charAt(c) == ' ' || json.charAt(c) == '\n' || json.charAt(c) == '\r' || json.charAt(c) == '\t')) {
			c--;
		}
	}
		return json.charAt(c);
	}
/**
 * Find the next char which isnt a space, tab or linebreak. This method allso fowards the head.
 * @return The next char which isn't a placeholder
 */
	private char getAnyNext() {
		if (head < json.length() - 1) {
			head++;
		}
		return json.charAt(head);
	}
/**
 * Reads to the next occurance of a given char which isn't escaped. This method forwards the head to the searched char.
 * @param c the char wanted
 * @return a String with all chars between the actual position and the searched char.
 */
	public String readToNotEscaped(char c) {
		String ret = "";
		while (json.charAt(head) == '\\' || json.charAt(head + 1) != c) {
			ret += getAnyNext();
		}
		return ret;
	}

	/**
	 * This method returns the everything from the actual position to the next occurrence of the closing brackets.
	 * @return The text to the closing barckets
	 */
	public String readToClosing(){
		int start=head;
		int end=head;
		int c=1;
		do{
			end=getNextBent(end+1);
			if(json.charAt(end)=='}'){
				c--;
			}else if(json.charAt(end)=='{'){
				c++;
			}else{
				System.out.println("Problem");
			}
		}while(c!=0);
		head=++end;
		return json.substring(start,end);
	}
	/**
	 * Reads to the next barcket
	 * @param c start index
	 * @return end index
	 */
	private int getNextBent(int c){
		int a=json.indexOf('{',c);
		int b=json.indexOf('}',c);
		if(a<b&&a>0){
			return a;
		}
		return b;
		
	}
/**
 * moves the head to the next occurrence of a given char.
 * @param c the char to jump to.
 */
	public void jumpTo(char c) {
		if (json.indexOf(c, head) == -1) {
			getNext();
		} else {
			head = json.indexOf(c, head);
		}
	}
/**
 * Checks if the end was reached.
 * @return true if the end is reached.
 */
	public boolean reachedEnd() {
		if (head == json.length() - 1) {
			return true;
		} else {
			return false;
		}
	}
/**
 * Get the text from the head till to the end.
 * @return the text not processed yet.
 */
	public String getRest() {
		return json.substring(head);
	}

}
