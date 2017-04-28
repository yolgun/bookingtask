package com.marketlogicsoftware;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SuggestionBuilderTest {

	/** The maximum amount of words which can be combined to a suggestion */
	private final static int MAX_COMBINED_TOKENS = 3;

	static final class Suggestion {
		private final String text;

		public Suggestion(String text) {
			this.text = text;
		}

		public String toString() {
			return text;
		}
	}

	/**
	 * Derives a list of suggestions from the given token stream. The given list
	 * of tokens reflect a sorted list of tokens of a text. Each token reflects
	 * either a single word or a punctuation mark like :.? A suggestion is
	 * either a single word or a combination of following words (delimited by a
	 * single space) and does not include any stopWord or a single character.
	 * Combined word suggestions can maximal include MAX_COMBINED_TOKENS of
	 * following words.
	 *  
	 * Example: Stop Words = {"is", "can", "the"}
	 * 
	 * Token Stream = {"The", "beautiful", "girl", "from", "the", "farmers", "market", ".", "I", "like", "chewing", "gum", "." } 
	 *
	 * Suggestions:
	 * "beautiful", 
	 * "beautiful girl", 
	 * "beautiful girl from", 
	 * "girl",
	 * "girl from", 
	 * "from", 
	 * "farmers", 
	 * "farmers market", 
	 * "market", 
	 * "like",
	 * "like chewing", 
	 * "like chewing gum", 
	 * "chewing", 
	 * "chewing gum", 
	 * "gum"
	 * 
	 */
	
	static Set<Suggestion> buildSuggestionsFromTokenStream(Iterator<String> tokens, Set<String> stopWords) {
		StringBuilder sb = new StringBuilder();
		Set<Suggestion> results = new HashSet<>();
		String sep = "";
		int counter = 0;
		for (Iterator<String> it = tokens; it.hasNext(); ) {
			String token = it.next();
			counter++;
			String result = sb.toString();
			if (!result.isEmpty()) {
				results.add(new Suggestion(result));
			}
			if (shouldStop(token, stopWords, counter)) {
				sb.setLength(0);
				sep = "";
				counter = 0;
			} else {
				sb.append(sep + token);
				sep = " ";
			}
		}
		String result = sb.toString();
		if (!result.isEmpty()) {
			results.add(new Suggestion(result));
		}
		return results;
	}

	private static boolean shouldStop(String word, Set<String> stopWords, int counter) {
		if (word.length() == 1 || stopWords.contains(word.toLowerCase()) || counter > MAX_COMBINED_TOKENS) {
			return true;
		}
		return false;
	}
}
