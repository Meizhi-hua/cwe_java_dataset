
package org.springframework.web.util;
public class JavaScriptUtils {
	public static String javaScriptEscape(String input) {
		if (input == null) {
			return input;
		}
		StringBuilder filtered = new StringBuilder(input.length());
		char prevChar = '\u0000';
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '"') {
				filtered.append("\\\"");
			}
			else if (c == '\'') {
				filtered.append("\\'");
			}
			else if (c == '\\') {
				filtered.append("\\\\");
			}
			else if (c == '/') {
				filtered.append("\\/");
			}
			else if (c == '\t') {
				filtered.append("\\t");
			}
			else if (c == '\n') {
				if (prevChar != '\r') {
					filtered.append("\\n");
				}
			}
			else if (c == '\r') {
				filtered.append("\\n");
			}
			else if (c == '\f') {
				filtered.append("\\f");
			}
			else if (c == '\b') {
				filtered.append("\\b");
			}
			else if (c == '\013') {
				filtered.append("\\v");
			}
			else if (c == '<') {
				filtered.append("\\u003C");
			}
			else if (c == '>') {
				filtered.append("\\u003E");
			}
			else if (c == '\u2028') {
				filtered.append("\\u2028");
			}
			else if (c == '\u2029') {
				filtered.append("\\u2029");
			}
			else {
				filtered.append(c);
			}
			prevChar = c;
		}
		return filtered.toString();
	}
}
