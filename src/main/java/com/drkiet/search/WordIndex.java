package com.drkiet.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordIndex implements Serializable {
	private static final long serialVersionUID = 3601898172054707997L;
	private String word;
	private List<Integer> pages = new ArrayList<Integer>();

	public WordIndex(String word, int pageNo) {
		this.word = cleanse(word.toLowerCase());
		pages.add(pageNo);
	}

	/**
	 * - a word can only be alphabet - a word may include a dash
	 * 
	 * @param word2
	 * @return
	 */
	public static String cleanse(String dirtyWord) {
		StringBuilder sb = new StringBuilder();
		dirtyWord = dirtyWord.trim().toLowerCase();

		if (dirtyWord.length() == 1 || dirtyWord.isEmpty() || skipProcessing(dirtyWord)) {
			return "";
		}

		for (int idx = 0; idx < dirtyWord.length(); idx++) {
			if (Character.isAlphabetic(dirtyWord.charAt(idx)) || dirtyWord.charAt(idx) == '-') {
				sb.append(dirtyWord.charAt(idx));
			}
		}

		return sb.toString();
	}

	/**
	 * Words to avoid indexing and searching.
	 */
	public static final String[] ARTICLES = { "the", "an" };
	public static final String[] PERSONAL_PRONOUNS = { "i", "you", "he", "she", "it", "we", "they", "me", "him", "her",
			"us", "them" };
	public static final String[] SUBJECTIVE_PRONOUNS = { "what", "who" };
	public static final String[] OBJECTIVE_PRONOUNS = { "me", "him", "her", "it", "us", "you", "them", "whom" };
	public static final String[] POSSESSIVE_PRONOUNS = { "mine", "yours", "his", "hers", "ours", "theirs" };
	public static final String[] DEMONSTRATIVE_PRONOUNS = { "this", "that", "these", "those" };
	public static final String[] INTERROGATIVE_PRONOUNS = { "who", "whom", "which", "what", "whose", "whoever",
			"whatever", "whichever", "whomever" };
	public static final String[] RELATIVE_PRONOUNS = { "who", "whom", "whose", "which", "that", "what", "whatever",
			"whoever", "whomever", "whichever" };
	public static final String[] REFLESIVE_PRONOUNS = { "myself", "yourself", "himself", "herself", "itself",
			"ourselves", "themselves" };
	public static final String[] NUMBERS = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
			"nine", "ten" };
	public static final String[] INDEFINITE_PRONOUNS = { "Anything", "everybody", "another", "each", "few", "many",
			"none", "some", "all", "any", "anybody", "anyone", "everyone", "everything", "no one", "nobody", "nothing",
			"none", "other", "others", "several", "somebody", "someone", "something", "most", "enough", "little",
			"more", "both", "either", "neither", "one", "much", "such" };

	public static final String[] MISC_WORDS_TO_SKIP = { "than",  };

	public static final String[][] WORDS_TO_SKIP = { INDEFINITE_PRONOUNS, NUMBERS, RELATIVE_PRONOUNS,
			REFLESIVE_PRONOUNS, INTERROGATIVE_PRONOUNS, DEMONSTRATIVE_PRONOUNS, POSSESSIVE_PRONOUNS, OBJECTIVE_PRONOUNS,
			SUBJECTIVE_PRONOUNS, PERSONAL_PRONOUNS, ARTICLES };

	private static boolean skipProcessing(String word) {
		for (String[] avoidList : WORDS_TO_SKIP) {
			for (String skipWord : avoidList) {
				if (skipWord.equalsIgnoreCase(word)) {
					return true;
				}
			}
		}

		return false;
	}

	public void add(int pageNo) {
		pages.add(pageNo);
	}

	public Iterator<Integer> getPages() {
		return pages.iterator();
	}

	public boolean contains(String searchWord) {
		return word.contains(searchWord);
	}
}
