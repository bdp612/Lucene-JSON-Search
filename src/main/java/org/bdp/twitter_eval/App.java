package org.bdp.twitter_eval;

public class App 
{
    public static void main( String[] args )
    {
    	// check number are arguments and run program if number is 1
    	if (Evaluator.evaluateArguments(args))
    	{
    		String filePath = args[0];
        	Evaluator.evaluateFolder(filePath);	
    	}
    }
}
