package validators;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

public class DropdownValidator {

	public static List<WebElement> findAllSelectDropdowns(WebDriver driver) {
		List<WebElement> dropdowns = null;

		try {
			dropdowns = driver.findElements(By.tagName("select"));
		} catch (Exception e) {
			System.out.println("Unable to get the dropdowns in the AUT, error message below:");
			e.getMessage();
		}

		return dropdowns;
	}

	public static WebElement findSelectDropdown(WebDriver driver, String xpath) {
		WebElement dropdown = null;

		try {
			dropdown = driver.findElement(By.xpath(xpath));
		} catch (Exception e) {
			System.out.println("Unable to get the dropdowns in the AUT, error message below:");
			e.getMessage();
		}

		return dropdown;
	}

	private static Select getSelectDropdown(WebElement dropdown) {
		Select selectDropdown = null;

		try {
			selectDropdown = new Select(dropdown);
		} catch (UnexpectedTagNameException e) {
			System.out.println("Not a SELECT element");
		} catch (Exception e) {
			System.out.println("Unable to get the select dropdown, error message below:");
			e.getMessage();
		}

		return selectDropdown;
	}

	public static boolean isSortedInNaturalOrder(WebElement dropdown) {
		boolean isSorted = true;

		Select selectDropdown = getSelectDropdown(dropdown);

		List<WebElement> options = selectDropdown.getOptions();
		String previous = "";

		for (WebElement option : options) {
			if (option.getText().compareTo(previous) < 0) {
				isSorted = false;
				break;
			}
			previous = option.getText();
		}
		return isSorted;
	}

	public static boolean isMultipleSelect(WebElement dropdown) {
		Select selectDropdown = getSelectDropdown(dropdown);

		return selectDropdown.isMultiple();
	}

	public static String getDefaultSelection(WebElement dropdown) {
		WebElement defaultOption = null;
		String defaultSelection = null;

		try {
			defaultOption = dropdown.findElement(By.xpath("//option[@selected = 'selected']"));
		} catch (Exception e) {
			System.out.println("No default option");
		}

		if (defaultOption != null)
			defaultSelection = defaultOption.getText();
		else
			defaultSelection = "No default option";

		return defaultSelection;
	}
}
