package org.bdp.twitter_eval;

import org.json.simple.JSONObject;

public class TweetObject {

	private String text;
	private String type;
	private String author;
	private String timeFrame;
	
	public TweetObject(JSONObject json)
	{
        try {    
            // text
            String text = (String) json.get("text");
            this.setText(text);
            
            // author
            JSONObject userObject = (JSONObject) json.get("user");
            String author = (String) userObject.get("screen_name");
            this.setAuthor(author);
            
            // time frame
            String createdAt = (String) json.get("created_at");
            String timeFrame = TweetHelper.determineTimeframe(createdAt);
            this.setTimeFrame(timeFrame);
            
    	} catch (NullPointerException ex) {
    		
    	}
	}
	
	public String getTimeFrame() {
		return timeFrame;
	}
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
