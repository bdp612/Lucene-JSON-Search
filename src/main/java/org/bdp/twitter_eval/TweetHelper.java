package org.bdp.twitter_eval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetHelper {

	public static String determineTextType(String word) 
	{   	
		String type = "";
		
		if(word.startsWith("#"))
    	{
    		type = AppConstants.HASHTAG;
    	}
    	else
    	{
    		type = AppConstants.WORD;
    	}
		
		return type;
	}
	public static String determineTimeframe(String createdAt) 
	{
    	String timeFrame = "";
    	
        String morningString = AppConstants.MORNINGTIME;
        String afternoonString = AppConstants.AFTERNOONTIME;
        String eveningString = AppConstants.EVENINGTIME;
        String latenightString = AppConstants.LATENIGHTTIME;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        
        String[] words = createdAt.split(" ");
        
        String time = words[AppConstants.SPACESBEFORETIMESTRING];
        
        Date morning;
		try 
		{
			morning = dateFormat.parse(morningString);
	        Date afternoon = dateFormat.parse(afternoonString);
	        Date evening = dateFormat.parse(eveningString);
	        Date latenight = dateFormat.parse(latenightString);
	        
	        Date tweetTime = dateFormat.parse(time);
	        
	        // morning, afternoon, evening, late night
	        if (tweetTime.after(latenight) && tweetTime.before(morning))
	        {
	        	timeFrame = AppConstants.LATENIGHT;
	        }
	        else if (tweetTime.after(morning) && tweetTime.before(afternoon))
	        {
	        	timeFrame = AppConstants.MORNING;
	        }
	        else if (tweetTime.after(afternoon) && tweetTime.before(evening))
	        {
	        	timeFrame = AppConstants.AFTERNOON;
	        } 
	        else 
	        {
	        	timeFrame = AppConstants.EVENING;
	        }  
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return timeFrame;
	}
	
}
