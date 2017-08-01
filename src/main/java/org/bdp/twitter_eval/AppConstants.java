package org.bdp.twitter_eval;

import java.util.Arrays;
import java.util.HashSet;

public class AppConstants {

	// Strings
	public static final String INDEXPATH="./index";

	public static final String WORD="word";
	public static final String HASHTAG="hashtag";
	
	public static final String MORNING="morning";
	public static final String AFTERNOON="afternoon";
	public static final String EVENING="evening";
	public static final String LATENIGHT="latenight";
	
	public static final String MORNINGTIME = "05:00:00";
	public static final String AFTERNOONTIME = "12:00:00";
	public static final String EVENINGTIME = "18:00:00";
	public static final String LATENIGHTTIME = "00:00:00";
	
	// ints
	public static final int SPACESBEFORETIMESTRING = 3;
	
	// hashsets
	public static final HashSet<String> EXCLUSIONLIST = 
			new HashSet<String>(Arrays.asList("", "-", ",", ":"));
	
	public static final HashSet<String> ADVANCEDEXCLUSIONLIST = 
			new HashSet<String>(Arrays.asList(
					"", "-", ",", ":", "the", "a", "de", "to", "you", "que", "in", "me", "I", "of",
					"for", "is", "and", "on", "-", "with", "en", "my", "your", "y",
					"la", "at", "be", "el", "this", "o", "all", "no", "they", "when",
					"about", "who", "it", "have", "that", "will", "back", "It's",
					"eu", "so", "you're", "é", "were", "es", "se", "los"
					));
	
	
}
