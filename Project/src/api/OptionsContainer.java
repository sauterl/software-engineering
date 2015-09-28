package api;

class OptionsContainer {
	
	private String name;
	private String textSnippet;
	private String before;
	private String after;
	
	/**
	 * @param name Data set name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * @param textSnippet Text contained in name or description.
 	 */
	public void setText(String textSnippet){
		this.textSnippet = textSnippet;
	}
	
	/**
	 * @param timestamp Timestamp in the format YYYY-MM-DD or YYYY-MM-DD HH:MM:SS
	 */
	public void setBefore(String timestamp){
		this.before = timestamp;
	}
	
	/**
	 * @param timestamp Timestamp in the format YYYY-MM-DD or YYYY-MM-DD HH:MM:SS
	 */
	public void setAfter(String timestamp){
		this.after = timestamp;
	}

}