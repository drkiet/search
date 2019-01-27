package com.drkiet.search;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * -Daccreader.folder=d:/ebooks/cpa
 * -Daccreader.references=Wiley_GAAP_for_Governments_2016.pdf
 * -Daccreader.workspace=C:/book-catalog/accreader
 * 
 * @author ktran
 *
 */
public class DocumentSearchTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentSearchTest.class);
	private static final String CONTENT_FILE = "d:/ebooks/cpa/Chapter_01_text.pdf";
	private static final String WORKSPACE_FOLDER = "C:/book-catalog/accreader";
	private DocumentSearch ds;
	private List<String> words;

	@Before
	public void setUp() {
		ds = DocumentSearch.getInstance(CONTENT_FILE, WORKSPACE_FOLDER);
		words = ds.getWordsInDocument();
	}

	@Test
	public void shouldLoadFiles() {

		for (String word : words) {
			Iterator<Integer> pages = ds.search(word);
			StringBuilder sb = new StringBuilder();
			while (pages.hasNext()) {
				sb.append(Integer.valueOf(pages.next())).append(", ");
			}

			LOGGER.info("{}: {}", word, sb.toString());
		}
		assertThat(ds, notNullValue());
	}

	@Test
	public void shouldFindSearchText() {
		Iterator<Integer> pages = ds.search("entit");
		String pageNumbersList = listOfIntegers(pages);
		LOGGER.info("pages: {}", pageNumbersList);
		assertThat(pageNumbersList, not(isEmptyOrNullString()));
	}

	@Test
	public void shouldNotFindSearchText() {
		Iterator<Integer> pages = ds.search("entitx");
		String pageNumbersList = listOfIntegers(pages);
		LOGGER.info("pages: {}", pageNumbersList);
		assertThat(pageNumbersList, isEmptyOrNullString());
	}

	private String listOfIntegers(Iterator<Integer> pages) {
		StringBuilder sb = new StringBuilder();
		while (pages.hasNext()) {
			sb.append(Integer.valueOf(pages.next())).append(", ");
		}
		return sb.toString();
	}
}
