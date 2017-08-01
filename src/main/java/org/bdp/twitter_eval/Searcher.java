package org.bdp.twitter_eval;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.misc.HighFreqTerms.DocFreqComparator;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher 
{

	private Directory indexDirectory = null;
	private IndexReader indexReader = null;
	private IndexSearcher indexSearcher = null;;
	private Analyzer analyzer = null;

	// searching methods
	public void retrieveTopFieldsByTimeFrame(String indexpath) 
	{
		try 
		{
			this.indexDirectory = FSDirectory.open(new File(AppConstants.INDEXPATH).toPath());
			this.indexReader = DirectoryReader.open(indexDirectory);
			this.indexSearcher = new IndexSearcher(indexReader);
			this.analyzer = new SimpleAnalyzer();

			// morning
			queryIndex(AppConstants.MORNING);
			queryIndex(AppConstants.WORD, AppConstants.MORNING);
			queryIndex(AppConstants.HASHTAG, AppConstants.MORNING);

			// afternoon
			queryIndex(AppConstants.AFTERNOON);
			queryIndex(AppConstants.WORD, AppConstants.AFTERNOON);
			queryIndex(AppConstants.HASHTAG, AppConstants.AFTERNOON);

			// evening
			queryIndex(AppConstants.EVENING);
			queryIndex(AppConstants.WORD, AppConstants.EVENING);
			queryIndex(AppConstants.HASHTAG, AppConstants.EVENING);

			// latenight
			queryIndex(AppConstants.LATENIGHT);
			queryIndex(AppConstants.WORD, AppConstants.LATENIGHT);
			queryIndex(AppConstants.HASHTAG, AppConstants.LATENIGHT);

		} 
		catch (IOException ex) 
		{
			System.out.println("The index path could not be found.\n");
		}
	}

	// query methods
	private void queryIndex(String timeFrame) 
	{
		try {
			// form the query
			String line = "timeFrame: " + timeFrame;
			QueryParser parser = new QueryParser("author", analyzer);
			Query query = parser.parse(line);

			// get the scoredocs
			// could not find a better method in the time I had to do this. only just learned lucene for this
			// ideally the results of this search would return a list strings from a specified field
			// then I could skip the steps below which translate the scoredocs to this list
			TopDocs results = indexSearcher.search(query, 300000);
			ScoreDoc[] hits = results.scoreDocs;

			// use scoredocs to get array of text fields from documents
			ArrayList<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>(Arrays.asList(hits));
			ArrayList<String> textArray = convertScoreDocsToAuthors(scoreDocs);

			// get the top ten of each word and display it
			List<Map.Entry<String, Long>> resultMap = getTopTenMostFrequent(textArray);
			displayTopTenAuthors(resultMap, timeFrame);
		} 
		catch (ParseException ex) 
		{
			ex.printStackTrace();
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private void queryIndex(String type, String timeFrame) 
	{
		try {
			// form the query
			String line = "type: " + type + " AND timeFrame: " + timeFrame;
			QueryParser parser = new QueryParser("type", analyzer);
			Query query = parser.parse(line);

			// get the scoredocs
			// could not find a better method in the time I had to do this. only just learned lucene for this
			// ideally the results of this search would return a list strings from a specified field
			// then I could skip the steps below which translate the scoredocs to this list
			TopDocs results = indexSearcher.search(query, 300000);
			ScoreDoc[] hits = results.scoreDocs;

			// use scoredocs to get array of text fields from documents
			ArrayList<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>(Arrays.asList(hits));
			ArrayList<String> textArray = convertScoreDocsToText(scoreDocs);

			// get the top ten of each word and display it
			List<Map.Entry<String, Long>> resultMap = getTopTenMostFrequent(textArray);
			displayTopTenText(resultMap, type, timeFrame);
		} 
		catch (ParseException ex) 
		{
			ex.printStackTrace();
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}

	// conversion methods
	private static ArrayList<String> convertScoreDocsToAuthors(ArrayList<ScoreDoc> scoreDocs) 
	{
		ArrayList<String> returnList = new ArrayList<String>();
		try 
		{
			Directory indexDirectory = FSDirectory.open(new File(AppConstants.INDEXPATH).toPath());
			IndexReader indexReader = DirectoryReader.open(indexDirectory);

			for (ScoreDoc scoreDoc : (List<ScoreDoc>) scoreDocs) 
			{
				String docText = indexReader.document(scoreDoc.doc).get("author");
				returnList.add(docText);
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return returnList;
	}
	private static ArrayList<String> convertScoreDocsToText(ArrayList<ScoreDoc> scoreDocs) 
	{
		ArrayList<String> returnList = new ArrayList<String>();
		try 
		{

			Directory indexDirectory = FSDirectory.open(new File(AppConstants.INDEXPATH).toPath());
			IndexReader indexReader = DirectoryReader.open(indexDirectory);

			for (ScoreDoc scoreDoc : (List<ScoreDoc>) scoreDocs) {
				String docText = indexReader.document(scoreDoc.doc).get("text");
				returnList.add(docText);
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return returnList;
	}

	// frequency methods
	private static List<Map.Entry<String, Long>> getTopTenMostFrequent(ArrayList<String> list) 
	{
		Map<String, Long> map = list.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		List<Map.Entry<String, Long>> result = map.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toList());

		return result;
	}

	// display methods
	private void displayTopTenAuthors(List<Entry<String, Long>> resultMap, String timeFrame) 
	{
		System.out.println("Top authors for the " + timeFrame + ":");
		resultMap.forEach(result -> System.out.println("@" + result.getKey() + " (" + result.getValue() + " tweets)"));
		System.out.println();
	}
	private void displayTopTenText(List<Map.Entry<String, Long>> resultMap, String type, String timeFrame) 
	{
		System.out.println("Top " + type + "s for the " + timeFrame + ":");
		resultMap.forEach(result -> System.out.println(result.getKey() + " (" + result.getValue() + " mentions)"));
		System.out.println();
	}

}
