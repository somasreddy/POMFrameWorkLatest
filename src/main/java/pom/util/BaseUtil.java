package pom.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import pom.base.BaseTest;

public class BaseUtil extends BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseUtil.class);

    // Get the text of a WebElement
    public String getText(WebElement element) {
	return element.getText();
    }

    // Get the title of the current page
    public String getPageTitle() {
	return getDriver().getTitle();
    }

    // Method to take a screenshot with optional custom name
    public static void takeScreenshot(String screenshotName) {
	try {
	    File screenshotDir = new File(System.getProperty("user.dir") + "/screenshots");
	    if (!screenshotDir.exists()) {
		screenshotDir.mkdirs(); // Create the directory if it doesn't exist
	    }

	    File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
	    String screenshotPath = screenshotDir + "/" + screenshotName + "_" + System.currentTimeMillis() + ".png";
	    File destFile = new File(screenshotPath);

	    // Save the screenshot to the destination path
	    FileUtils.copyFile(srcFile, destFile);
	    logger.info("Screenshot taken and saved to: " + screenshotPath);
	} catch (IOException e) {
	    logger.error("Error taking screenshot: " + e.getMessage(), e);
	}
    }

    // Overloaded method for default screenshot naming
    public static void takeScreenshot() {
	takeScreenshot("screenshot");
    }

    // Method to write item details to a file
    public void writeToFile(String itemName, String itemPrice) {
	String filePath = System.getProperty("user.dir") + "/src/main/java/pom/config/ItemDetails.properties"; // Specify the path
	File file = new File(filePath);
	try {
	    // Create the file if it doesn't exist
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    // Use FileWriter without the append flag to clear existing content
	    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) { // 'false' to overwrite
		out.println("Item_Name: " + itemName);
		out.println("Item_Price: " + itemPrice);
		out.println(); // Add a newline for readability
		logger.info("Item details written to file: " + filePath);
	    }
	} catch (IOException e) {
	    logger.error("Error writing to file: " + e.getMessage(), e);
	}
    }
}
