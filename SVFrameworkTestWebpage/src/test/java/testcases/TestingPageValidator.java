package testcases;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import util.Utility;
import validators.DropdownValidator;
import validators.UrlValidator;
import validators.WebpageSpellChecker;

public class TestingPageValidator {

	protected static WebDriver driver;
	private static String autUrl = Utility.extractAutUrl(System.getProperty("user.home")+"\\Downloads\\auturl.txt");
	private Map<String, String> misspelledSuggestions;

	@BeforeClass
	public static void setUp() {
		String driverName = "webdriver.chrome.driver";
		String driverPath = System.getProperty("user.dir")+"\\src\\test\\resources\\drivers\\chromedriver.exe";
		System.setProperty(driverName, driverPath);
		driver = new ChromeDriver();
		driver.get(autUrl);
		driver.manage().window().maximize();
	}

	@Test
	public void hasSiteMap() {
		System.out.println("Test Name: Website has a site map");
		
		WebElement link = driver.findElement(By.id("hasSiteMap"));
		String linkHref = link.getAttribute("href");
		boolean hasSiteMap = UrlValidator.doesSitemapExist(linkHref, driver);
		
		Assert.assertTrue(hasSiteMap);
	}
	
	@Test
	public void hasNoSiteMap() {
		System.out.println("Test Name: Website has no site map");
		
		WebElement link = driver.findElement(By.id("hasNoSiteMap"));
		String linkHref = link.getAttribute("href");
		boolean hasSiteMap = UrlValidator.doesSitemapExist(linkHref, driver);
		
		Assert.assertTrue(hasSiteMap);
	}
	
	@Test
	public void checkSpellings() {
		System.out.println("Test Name: Spell check");
		
		String webpageText = null;
		WebpageSpellChecker jazzySpellChecker = new WebpageSpellChecker();

		try {
			webpageText = driver.findElement(By.id("textcontent")).getText();
		} catch (Exception e) {
			System.out.println("Unable to extract the text error message below:");
			e.getMessage();
		}

		misspelledSuggestions = jazzySpellChecker.showSuggestions(webpageText);

		for (Entry<String, String> suggestion : misspelledSuggestions.entrySet()) {
			System.out.println("Misspelled word: " + suggestion.getKey());
			System.out.println("Correction suggestions: " + suggestion.getValue());
		}
		
		Assert.assertTrue(misspelledSuggestions.isEmpty());
	}

	@Test
	public void responseOfBrokenLink() {
		System.out.println("Test Name: Broken Link Response");
		
		WebElement link = driver.findElement(By.id("brokenLink"));
		String linkHref = link.getAttribute("href");
//		String response = UrlValidator.getHttpsResponse(linkHref);
		int response = UrlValidator.getHttpsResponseCode(linkHref);
		
		System.out.println("Response to: " + linkHref + " is: " + response);
		
		Assert.assertTrue(response < 400);
	}

	@Test
	public void responseOfGoodLink() {
		System.out.println("Test Name: Functional Link Response");
		
		WebElement link = driver.findElement(By.id("goodLink"));
		String linkHref = link.getAttribute("href");
//		String response = UrlValidator.getHttpsResponse(linkHref);
		int response = UrlValidator.getHttpsResponseCode(linkHref);

		System.out.println("Response to: " + linkHref + " is: " + response);
		
		Assert.assertTrue(response < 400);
	}

	@Test
	public void validateDefaultSortedDropdown() {	
		System.out.println("Test Name: Validate a sorted dropdown with a default option");
		
		String xpath = "//*[@id='sorted_hasDefault_single']";

		WebElement selectDropdown = DropdownValidator.findSelectDropdown(driver, xpath);

		Assert.assertTrue(DropdownValidator.isSortedInNaturalOrder(selectDropdown));
		Assert.assertFalse(DropdownValidator.getDefaultSelection(selectDropdown).equalsIgnoreCase("No default option"));
		Assert.assertFalse(DropdownValidator.isMultipleSelect(selectDropdown));
	}

	@Test
	public void validateDefaultUnsortedDropdown() {
		System.out.println("Test Name: Validate an unsorted dropdown with a default option");
		
		String xpath = "//*[@id='unsorted_hasDefault_single']";

		WebElement selectDropdown = DropdownValidator.findSelectDropdown(driver, xpath);

		Assert.assertTrue(DropdownValidator.isSortedInNaturalOrder(selectDropdown));
	}

	@Test
	public void validatenoDefaultUnsortedDropdown() {
		System.out.println("Test Name: Validate an unsorted dropdown with no default option is multi select");
		
		String xpath = "//*[@id='unsorted_noDefault_single']";

		WebElement selectDropdown = DropdownValidator.findSelectDropdown(driver, xpath);

		Assert.assertTrue(DropdownValidator.isMultipleSelect(selectDropdown));
	}

	@AfterClass
	public static void shutDown() {
		driver.close();
		driver.quit();
	}
}
