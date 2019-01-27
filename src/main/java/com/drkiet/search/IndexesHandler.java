package com.drkiet.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexesHandler implements Serializable {
	private static final long serialVersionUID = -9181180220788271377L;

	private String documentName;
	private HashMap<Character, HashMap<String, WordIndex>> indexes;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public IndexesHandler(List<List<String>> wordsByPages) {
		makeIndexes(wordsByPages);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(WordIndex.class);

	public void makeIndexes(List<List<String>> wordsByPages) {
		indexes = new HashMap<Character, HashMap<String, WordIndex>>();

		for (int idx = 0; idx < wordsByPages.size(); idx++) {

			for (String word : wordsByPages.get(idx)) {
				String cleansedWord = WordIndex.cleanse(word);
				LOGGER.info("{}:{}", word, cleansedWord);

				if (cleansedWord.isEmpty()) {
					LOGGER.info("*** NOT INDEXING {} ***", cleansedWord);
					continue;
				}

				Character indexChar = new Character(cleansedWord.charAt(0));

				HashMap<String, WordIndex> firstCharIndexes = indexes.get(indexChar);
				if (firstCharIndexes == null) {
					indexes.put(indexChar, new HashMap<String, WordIndex>());
					firstCharIndexes = indexes.get(indexChar);
				}

				WordIndex wordIndex = firstCharIndexes.get(cleansedWord);

				if (wordIndex == null) {
					wordIndex = new WordIndex(word, idx + 1);
					firstCharIndexes.put(cleansedWord, wordIndex);
				} else {
					wordIndex.add(idx + 1);
				}

			}

		}
	}

	public List<String> getAllIndexWords() {
		Iterator<Character> firstChars = indexes.keySet().iterator();
		List<String> words = new ArrayList<String>();

		while (firstChars.hasNext()) {
			Character charIndex = firstChars.next();
			HashMap<String, WordIndex> firstCharIndexes = indexes.get(charIndex);
			firstCharIndexes.keySet().forEach(word -> {
				words.add(word);
			});
		}
		return words;
	}

	/**
	 * Looking for words starts with the search word
	 * 
	 * @param searchWord
	 * 
	 * @return a list of sorted page numbers.
	 */
	public Iterator<Integer> getPageNumbersThatContains(String searchWord) {
		String cleansedWord = WordIndex.cleanse(searchWord);

		if (cleansedWord.isEmpty()) {
			return new ArrayList<Integer>().iterator();
		}

		Character indexChar = new Character(cleansedWord.charAt(0));

		HashMap<String, WordIndex> firstCharIndexes = indexes.get(indexChar);

		if (firstCharIndexes == null) {
			return new ArrayList<Integer>().iterator();
		}

		Iterator<String> it = firstCharIndexes.keySet().iterator();
		HashMap<Integer, WordIndex> pageNoMap = new HashMap<Integer, WordIndex>();

		while (it.hasNext()) {
			WordIndex wordIndex = firstCharIndexes.get(it.next());
			if (wordIndex.contains(cleansedWord)) {
				Iterator<Integer> pageNoIt = wordIndex.getPages();
				while (pageNoIt.hasNext()) {
					pageNoMap.put(pageNoIt.next(), wordIndex);
				}
			}
		}
		List<Integer> pageNoList = new ArrayList<Integer>(pageNoMap.keySet());
		Collections.sort(pageNoList);
		return pageNoList.iterator();
	}

}
