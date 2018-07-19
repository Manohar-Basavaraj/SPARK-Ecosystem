package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utility {

	public static String extractAutUrl (String urlFilePath) {
		return readTextFile(urlFilePath);
	}
	
	public static String readTextFile (String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	            contentBuilder.append(sCurrentLine).append("\n");
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
}
