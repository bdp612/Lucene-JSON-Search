package org.bdp.twitter_eval;

public class Evaluator {

	public static boolean evaluateArguments(String[] args)
	{
		// if 0 arguments, or more than 1, return false
    	if (args.length < 1 || args.length > 1)
    	{
    		System.out.println("Please enter one argument, representing the folder path.");
    		return false;
    	}
    	
    	// if 1 argument, return true
    	return true;
	}
	public static void evaluateFolder(String filePath) {
		    	
		// create start time to track duration
        long programStartTime = System.currentTimeMillis();
        
    	// index the files
        System.out.println("Creating index with Lucene\n");
        long indexStartTime = System.currentTimeMillis();
        Indexer is = new Indexer();
    	is.createIndex(filePath);
    	long indexEndTime = System.currentTimeMillis();
        System.out.println("Total Indexing Time: " + (indexEndTime - indexStartTime) / 1000 +" seconds.\n");
        
        // search the index
        long searchStartTime = System.currentTimeMillis();
        Searcher ss = new Searcher();
    	ss.retrieveTopFieldsByTimeFrame(AppConstants.INDEXPATH);
    	long searchEndTime = System.currentTimeMillis();
        System.out.println("Total Search Time: " + (searchEndTime - searchStartTime) / 1000 +" seconds.\n");
        
    	// print final information for user
        long programEndTime = System.currentTimeMillis();    
        System.out.println("Total Program Time: " + (programEndTime - programStartTime) / 1000 +" seconds.\n");
        
	}

}
