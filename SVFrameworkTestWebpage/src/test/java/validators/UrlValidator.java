package validators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UrlValidator {
	public static String siteMapUrl = null;
	
	public static Map<String, Integer> validateLinks (List<WebElement> links) {
		Map<String, Integer> responses = new HashMap<String, Integer>();
		int responseCode = 0;
		String linkUrl = null;
		
		for(WebElement link : links) {
			linkUrl = link.getAttribute("href");
			responseCode = getHttpsResponseCode(linkUrl);
			
			responses.put(linkUrl, responseCode);
		}
		
		return responses;
	}
	
	public static void displayLinkResponses (Map<String, Integer> responses) {
		String linkUrl = null;
		Integer responseCode = 0;
		
		for (Entry<String, Integer> response : responses.entrySet()) {
			linkUrl = response.getKey();
			responseCode = response.getValue();
			
			System.out.println("Link: " + linkUrl + "\n Response : "+ responseCode);
			
			if(responseCode < 400) 
				System.out.println("Success/Redirect");
			if((responseCode >= 400) && (responseCode < 500))
				System.out.println("Client error");
			if ((responseCode >= 500) && (responseCode < 600))
				System.out.println("Server error");
		}
	}
	
	public static int getHttpsResponseCode (String linkUrl) {
		int responseCode = 0;
		URL url = null;

		HttpURLConnection connection = null;
		
		try {
			url = new URL(linkUrl);
			
			connection = (HttpsURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.connect();
			
			responseCode = connection.getResponseCode();
		} catch (IOException ioExcep) {
			ioExcep.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}

		return responseCode;
	}
	
	public static String getHttpsResponse (String linkUrl) {
		String response = null;
		URL url = null;

		HttpURLConnection connection = null;
		
		try {
			url = new URL(linkUrl);
			
			connection = (HttpsURLConnection) url.openConnection();
			response = connection.getResponseMessage();
			connection.disconnect();
		} catch (IOException ioExcep) {
			ioExcep.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}

		return response;
		
	}
	
	public static int getResponseCode (String linkUrl) {
		int responseCode = 0;
		URL url = null;
		String protocol = linkUrl.substring(0, linkUrl.indexOf(":"));
		HttpURLConnection connection = null;
		
		try {
			url = new URL(linkUrl);
			
			if (protocol.equalsIgnoreCase("https"))
				connection = (HttpsURLConnection) url.openConnection();
			if (protocol.equalsIgnoreCase("http"))
				connection = (HttpURLConnection) url.openConnection();
			
			responseCode = connection.getResponseCode();
		} catch (IOException ioExcep) {
			ioExcep.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}

		return responseCode;
	}
	
	public static boolean doesSitemapExist(String autUrl, WebDriver driver) {
		String[] siteMapUrls = {autUrl+"/sitemap.xml" , autUrl+"/sitemap/"};
		String[] siteMapTexts = {"'Sitemap'","'sitemap'", "'Site map'"};		
		WebElement siteMap = null;
				
		for (String siteMapUrl : siteMapUrls) {
//			System.out.println("Url tested: "+siteMapUrl);
			if (getResponseCode(siteMapUrl) < 400) {
				UrlValidator.siteMapUrl = siteMapUrl;
				return true;
			}
		}
		
		for (String siteMapText : siteMapTexts) {
			try {
				siteMap = driver.findElement(By.xpath("//a[contains(text()," + siteMapText + ")]"));
			}
			catch(Exception e) {
				e.getMessage();
			}
			if (siteMap != null) {
				UrlValidator.siteMapUrl = siteMap.getAttribute("href");
				return true;
			}
		}
		
		return false;
	}
}