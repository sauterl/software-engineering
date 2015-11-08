package ch.unibas.informatik.hs15.cs203.datarepository.common;

/**
 * Some utils for printing.
 * @author Loris
 *
 */
public class PrintUtils {

	private PrintUtils(){
		// only static methods
	}
	
	/**
	 * Replaces in the given string, all occurrences of the tab control sequence (<code>\t</code>)
	 * with the given amount of spaces.<br />
	 * For example invoking this method with that string:
	 * <pre>
	 * A\tTAB
	 * </pre>
	 * and the <tt>nbSpaces==4</tt>would transform to:
	 * <pre>
	 * A    TAB
	 * </pre>
	 * @param str The source string to replace the tabs in
	 * @param nbSpaces The amount of spaces with which the tab should get replaced
	 * @return The literally same string, but the tabs replaced.
	 */
	public static String replaceTabBySpaces(String str, int nbSpaces){
		String tab = "";
		for(int i=0; i<nbSpaces; i++){
			tab.concat(" ");
		}
		return str.replaceAll("\t", tab);
	}
	
	/**
	 * Replaces in the givne string all occurrences of <code>\t</code> with four spaces.
	 * <br />
	 * A call of this method returns the exact same value as invoking {@link #replaceTabBySpaces(String, int)}
	 * with <tt>str</tt> as string parameter and <tt>4</tt> as integer parameter.
	 * @param str The string whichs tabs get replaced
	 * @return Literally the same string, but with the tabs replaced.
	 */
	public static String replaceTabBySpaces(String str){
		return replaceTabBySpaces(str, 4);
	}
	
	public static String wrapLines(String str, int length){
		if(str.contains("\n")){
			return wrapLinesInternal(str.split("\n"), length);
		}else{
			return null;
		}
	}
	
	private static String wrapLinesInternal(String str, int length){
		if(str.length() <= length){
			return str;
		}
		StringBuilder sb = new StringBuilder();
		String rest = "";
		String curr = "";
		int index = str.length();
		
		return sb.toString();
	}
	
	private static String wrapLinesInternal(String[] lines, int length){
		StringBuilder sb = new StringBuilder();
		String rest = "";
		for(String l : lines){
			String s = rest + l;
			if(s.length() <= length){
				sb.append(s);
				sb.append("\n");
			}else{
				int index = s.lastIndexOf(" ", length); //herzlichen Dank an Silvan Stich!
				if(index > s.length() ){
					rest = s.substring(index);
					sb.append(s.substring(0, index));
				}else{
					rest = "";
					sb.append(s);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
