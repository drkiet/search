package com.drkiet.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.TextApp;
import com.drkiettran.text.model.Document;

public class DocumentSearch implements Serializable {
	private static final long serialVersionUID = 5535612535410645209L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentSearch.class);
	private String contentFileName;
	private List<List<String>> wordsByPages;
	private IndexesHandler indexesHandler;

	public static DocumentSearch getInstance(String contentFileName, String workspaceFolder) {
		LOGGER.info("Creating Document Search instance for {} at {}", contentFileName, workspaceFolder);
		String fullIndexFileName = getFullIndexFileName(contentFileName, workspaceFolder);

		try {
			if (indexFound(fullIndexFileName)) {
				return deserialize(fullIndexFileName);
			}
			DocumentSearch ds = new DocumentSearch(contentFileName);
			serializeIndexes(fullIndexFileName, ds);
			return ds;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static DocumentSearch deserialize(String fullIndexFileName) throws IOException, ClassNotFoundException {
		LOGGER.info("Deserialize: {}", fullIndexFileName);
		FileInputStream file = new FileInputStream(fullIndexFileName);
		ObjectInputStream in = new ObjectInputStream(file);

		// Method for deserialization of object
		DocumentSearch ds = (DocumentSearch) in.readObject();

		in.close();
		file.close();
		return ds;
	}

	private DocumentSearch(String contentFileName) {
		LOGGER.info("content {} and workspace {}", contentFileName);
		this.contentFileName = contentFileName;

		try {
			loadIndexes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadIndexes() throws IOException, ClassNotFoundException {
		createIndexesFromContent();
		return;
	}

	private static boolean indexFound(String indexFile) {
		return new File(indexFile).exists();
	}

	private static void serializeIndexes(String fullIndexFileName, DocumentSearch ds) throws IOException {
		LOGGER.info("Serialize: {}", fullIndexFileName);
		FileOutputStream file = new FileOutputStream(fullIndexFileName);
		ObjectOutputStream out = new ObjectOutputStream(file);

		out.writeObject(ds);

		out.close();
		file.close();
	}

	private void createIndexesFromContent() {
		Document document = loadDocumentFromFile();
		wordsByPages = document.getWordsByPages();
		LOGGER.info("{} pages found and:", wordsByPages.size());
		for (List<String> wordsInPage : wordsByPages) {
			LOGGER.info("{} words", wordsInPage.size());
		}
		indexesHandler = new IndexesHandler(wordsByPages);
	}

	public static String getFullIndexFileName(String contentFileName, String workspaceFolder) {
		String fileNameWithoutExt = FilenameUtils.removeExtension(FilenameUtils.getName(contentFileName));
		String indexFileName = fileNameWithoutExt + ".idx";
		return workspaceFolder + File.separator + indexFileName;
	}

	private Document loadDocumentFromFile() {
		TextApp textApp = new TextApp();

		Document document = textApp.getPages(contentFileName);
		if (document == null) {
		} else {
			LOGGER.info("{} has {} pages", contentFileName, document.getPageCount());
		}

		document.setBookFileName(contentFileName);
		return document;
	}

	public List<String> getWordsInDocument() {
		return indexesHandler.getAllIndexWords();
	}

	public Iterator<Integer> search(String word) {
		return indexesHandler.getPageNumbersThatContains(word);
	}

}
