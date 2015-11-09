package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Utilities {
	
	private Utilities(){/*no objects */}
	
	public static String wrapLine(String toFormat, int length){
		return wrapLine(toFormat, length, null);
	}
	
	public static String wrapLine(String toFormat, int length, Locale locale){
		StringBuilder sb = new StringBuilder();
		BreakIterator breaker = null;
		if(locale == null){
			breaker = BreakIterator.getLineInstance();
		}else{
			BreakIterator.getLineInstance(locale);
		}
		breaker.setText(toFormat);
		int previous = breaker.first();
		int next = breaker.next();
		int len = 0;
		while(next != BreakIterator.DONE){
			String curr = toFormat.substring(previous, next);
			len += curr.length();
			if(len >= length){
				sb.append("\n");
				len = curr.length();
			}else if(curr.endsWith("\n")){
				len = curr.length();
			}
			sb.append(curr);
			previous = next;
			next = breaker.next();
		}
		return sb.toString();
	}
	
	public static String wrapLinesSensitive(String text, int length, Locale locale){
		throw new UnsupportedOperationException("Not implemented yet");
		/*String wrapped = wrapLine(text, length, locale);
		StringBuilder out = new StringBuilder();
		ArrayList<String> lines = new ArrayList<String>(Arrays.asList(wrapped.split("\n")));
		for(int i=1; i<lines.size(); i++){
			int c = countPrecedingTabs(lines.get(i-1));
			if(c>0){
				StringBuilder tmp = new StringBuilder(lines.get(i));
				for(int j=0; j<c; j++){
					tmp.insert(0, "\t");
				}
				lines.set(i, tmp.toString());
			}
		}
		return out.toString();*/
	}
	
	public static int countPrecedingTabs(StringBuilder sb){
		return countPrecedingTabs(sb.toString() );
	}
	
	public static int countPrecedingTabs(String str){
		String TAB = "\t";
		int i = 0;
		int prev = str.indexOf(TAB);
		while(prev > 0){
			prev = str.indexOf(TAB, prev);
			i++;
		}
		return i;
	}
	
	@SuppressWarnings("unused")
	private static String concatLines(ArrayList<String> lines){
		StringBuilder out = new StringBuilder();
		for(String sb : lines){
			out.append(sb.toString());
			out.append("\n");
		}
		return out.toString();
	}
}
