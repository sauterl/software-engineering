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
		StringBuilder sb = new StringBuilder();
		BreakIterator breaker = null;
		int tabCount = 0;
		int oldTab = tabCount;
		if(locale == null){
			breaker = BreakIterator.getLineInstance();
		}else{
			BreakIterator.getLineInstance(locale);
		}
		breaker.setText(text);
		int previous = breaker.first();
		int next = breaker.next();
		int len = 0;
		while(next != BreakIterator.DONE){
			String curr = text.substring(previous, next);
			if(curr.equals("\t")){
				tabCount++;
				oldTab = tabCount;
			}
			len += curr.length();
			if(len >= length){
				sb.append("\n");
				StringBuilder tmp = new StringBuilder(curr);
				tabCount=0;
				for(int i=0; i<oldTab; i++){
					tmp.insert(0, "\t");
					if(i==oldTab-1){
						tmp.insert(oldTab, " ");
					}
					
				}
				curr = tmp.toString();
				len = curr.length();
			}else if(curr.endsWith("\n")){
				len = curr.length();
				tabCount = 0;
				oldTab = 0;
			}
			sb.append(curr);
			previous = next;
			next = breaker.next();
		}
		return sb.toString();
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
