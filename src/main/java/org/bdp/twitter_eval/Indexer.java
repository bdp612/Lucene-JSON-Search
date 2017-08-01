package org.bdp.twitter_eval;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Indexer 
{
	private IndexWriter indexWriter = null;
	boolean incorrectFile = false;

	// indexing methods
	public void createIndex(String filePath)
	{
		ArrayList<String> listOfZipPaths = new ArrayList<String>();;
		ArrayList<InputStream> listOfJsons = new ArrayList<InputStream>();

		listOfZipPaths = FileHelper.getFolderContents(filePath);
		int fileNumber = 0;
		int totalZipFiles = listOfZipPaths.size();
		
		// begin indexing
		openIndex();
		for (String zipPath : (List<String>) listOfZipPaths) 
		{
			this.incorrectFile = false;
			fileNumber++;
			System.out.println("Indexing file " + fileNumber + " of " + totalZipFiles + ".");
			listOfJsons = FileHelper.getZipContents(zipPath, fileNumber);
			listOfJsons.forEach( jsonFile -> addJsonContentsToIndex(jsonFile));
			if (incorrectFile == true)
			{
				System.out.println("ERROR: The zip content contains some invalid files.");
			}
		}
		finish();
	}
	private void addJsonContentsToIndex(InputStream jsonFilePath) 
	{
		// parse JSON file to JSON objects array
		JSONArray jsonObjects = FileHelper.parseJSONFile(jsonFilePath);
		
		// for each jsonObject, make a tweet object
		// add each word of that tweet object to the index
		try
		{
			for(JSONObject jsonObject : (List<JSONObject>) jsonObjects)
			{            	
				TweetObject tweet = new TweetObject(jsonObject);
	            addTweetTextToIndex(tweet);
			}
		}
		catch (NullPointerException ex)
		{
			this.incorrectFile = true;
		}
	}
	private void addTweetTextToIndex(TweetObject tweet) 
	{
		// split tweet text into an array of words
        String[] words = tweet.getText().split(" ");    
        String type = "";
        
        // remove the first gibberish word
        words[0] = "";
        
        // for each word (except the first), add a document to the index
        for ( String word : words)
        {
        	Document doc = new Document();

        	// skip any words in the exclusion list
        	if (Indexer.isWordInExclusionList(word))
        	{
        		continue;
        	}
        	
        	// remove any commas at the end of the word
        	// increases legitimacy of word frequency
        	if (word.endsWith(","))
        	{
        		// a single comma is in the exclusion list
        		word = word.substring(0, word.length() - 1);
        	}
        	
        	doc.add(new StringField("text", word, Field.Store.YES));
        	type = TweetHelper.determineTextType(word);
        	doc.add(new StringField("type", type, Field.Store.YES));
        	doc.add(new StringField("author", tweet.getAuthor(), Field.Store.YES));
        	doc.add(new StringField("timeFrame", tweet.getTimeFrame(), Field.Store.YES));
        	
            try 
            {
                indexWriter.addDocument(doc);
            } 
            catch (IOException ex) 
            {
                System.err.println("Error adding documents to the index. " +  ex.getMessage());
            }
         }
	}
	public static boolean isWordInExclusionList(String word) 
	{
		if (AppConstants.EXCLUSIONLIST.contains(word))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static boolean isWordInAdvancedExclusionList(String word) 
	{
		if (AppConstants.ADVANCEDEXCLUSIONLIST.contains(word))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// open/close methods
	private void openIndex() 
	{
        try 
        {
            Directory dir = FSDirectory.open(new File(AppConstants.INDEXPATH).toPath());
            Analyzer analyzer = new SimpleAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            //Always overwrite the directory
            iwc.setOpenMode(OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);

        } 
        catch (Exception e) 
        {
            System.err.println("Error opening the index. " + e.getMessage());
        }
	}	
	private void finish() 
	{
        try 
        {
        	indexWriter.commit();
        	indexWriter.close();       	
        } 
        catch (IOException ex) 
        {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
	}
	
}
