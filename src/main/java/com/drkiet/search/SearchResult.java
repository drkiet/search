package com.drkiet.search;

import java.util.List;

import com.drkiettran.text.model.Page;
import com.drkiettran.text.model.Word;

public class SearchResult {
	private List<Page> foundPages;
	private String searchText;
	private List<List<Word>> foundWords;

	public void addFoundWords(Page page, List<Word> words) {

	}
	
	public List<Integer> getFoundPageNumbers() {
		return null;
	}
	
	
}
