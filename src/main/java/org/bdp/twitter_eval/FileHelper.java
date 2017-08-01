package org.bdp.twitter_eval;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class FileHelper {

	public static ArrayList<String> getFolderContents(String zipFilePath)
	{
		ArrayList<String> listOfZipPaths = new ArrayList<String>();
		
		try
		{
	        byte[] buffer = new byte[2048];
	        
	        File folder = new File(zipFilePath);
	        ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
	
	        listOfFiles.forEach( file -> listOfZipPaths.add(file.getPath()));
		}
		catch (NullPointerException ex)
		{
			System.out.println("ERROR: The file path could not be read.");
			System.exit(0);
		}
        return listOfZipPaths;
		
	}
	public static ArrayList<InputStream> getZipContents(String zipFilePath, int fileNumber) 
	{    
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();
		
		try
		{
		    ZipFile zipFile = new ZipFile(zipFilePath);
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();

		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        InputStream jsonStream = zipFile.getInputStream(entry);
		        jsonFiles.add(jsonStream);
		    }	
		}
		catch (IOException ex)
		{
			System.out.println("ERROR: File " + fileNumber + " could not be read.");
		}
		
		return jsonFiles;
	}
	public static JSONArray parseJSONFile(InputStream jsonFile) 
	{
        //Get the JSON file
        Reader readerJson = new InputStreamReader(jsonFile);

        //Parse the JSON file using simple-json library
        Object fileObjects= JSONValue.parse(readerJson);
        JSONArray arrayObjects=(JSONArray)fileObjects;

        return arrayObjects;
	}

	
}
