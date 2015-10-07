package util.jsontools;

public class JsonReader {
	private String json;
	private int head = 0;

	public JsonReader(String json) {
		this.json = json;
	}

	public char getHead() {
		// System.out.print(json.charAt(head+1));
		return json.charAt(head);
	}

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

	private char getAnyNext() {
		if (head < json.length() - 1) {
			head++;
		}
		return json.charAt(head);
	}

	public String readToNotEscaped(char c) {
		String ret = "";
		while (json.charAt(head) == '\\' || json.charAt(head + 1) != c) {
			ret += getAnyNext();
		}
		return ret;
	}

//	public String readToClosing() {
//		String ret = "";
//		ret += getHead();
//		int c = 1;
//		do {
//			ret += getAnyNext();
//			if (getHead() == '{') {
//				c++;
//			}
//			if (getHead() == '}') {
//				c--;
//			}
//		} while (c != 0 || getHead() != '}');
//		return ret;
//	}
	
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
	
	private int getNextBent(int c){
		int a=json.indexOf('{',c);
		int b=json.indexOf('}',c);
		if(a<b&&a>0){
			return a;
		}
		return b;
		
	}

	public void jumpTo(char c) {
		if (json.indexOf(c, head) == -1) {
			getNext();
		} else {
			head = json.indexOf(c, head);
		}
	}

	public boolean reachedEnd() {
		if (head == json.length() - 1) {
			return true;
		} else {
			return false;
		}
	}

	public String getRest() {
		return json.substring(head);
	}

}
