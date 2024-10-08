package pom.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties setUProp;
    public static Properties fileProp;
    public static WebDriverWait wait;

    // Constructor to load properties from the configuration file
    public BaseTest() {
        try {
            setUProp = new Properties();
            FileInputStream fileIN = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/java/pom/config/config.properties");
            setUProp.load(fileIN);
        } catch (IOException e) {
            System.err.println("Error loading config properties: " + e.getMessage());
            throw new RuntimeException("Could not load configuration properties.", e);
        }
        try {
            fileProp = new Properties();
            FileInputStream fileIN = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/java/pom/config/ItemDetails.properties");
            fileProp.load(fileIN);
        } catch (IOException e) {
            System.err.println("Error loading config properties: " + e.getMessage());
            throw new RuntimeException("Could not load configuration properties.", e);
        }
    }

    // Get the current WebDriver instance for the thread
    public static WebDriver getDriver() {
        return driver.get();
    }

    // Browser initialization based on configuration
    @BeforeSuite
    public void BrowserInitialize() {
        String browser = setUProp.getProperty("browser");

        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver.set(new ChromeDriver());
                    break;
                case "firefox":
                    driver.set(new FirefoxDriver());
                    break;
                case "edge":
                    driver.set(new EdgeDriver());
                    break;
                case "safari":
                    driver.set(new SafariDriver());
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }

            // Maximize window and set timeouts
            getDriver().manage().window().maximize();
            getDriver().manage().deleteAllCookies();
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(setUProp.getProperty("pltimeout"))));
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(setUProp.getProperty("impWait"))));

            // Initialize WebDriverWait
            wait = new WebDriverWait(getDriver(), Duration.ofSeconds(Long.parseLong(setUProp.getProperty("wait"))));

            // Open the URL from properties file
            getDriver().get(setUProp.getProperty("url"));

        } catch (Exception e) {
            System.err.println("Browser initialization failed: " + e.getMessage());
            throw new RuntimeException("Could not initialize the browser.", e);
        }
    }

    // After each test method, take a screenshot if the test failed
    @AfterMethod
    public void tearDownAndTakeScreenshotOnFailure(ITestResult testResult) throws IOException {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot on test failure
            File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "./errorScreenshots/" + testResult.getName() + "-" + System.currentTimeMillis() + ".jpg";
            FileUtils.copyFile(scrFile, new File(screenshotPath));

            // Log failure and screenshot path in console
            System.err.println("Test failed: " + testResult.getName());
            System.err.println("Screenshot saved at: " + screenshotPath);
        } else {
            // Log pass in console
            System.out.println("Test passed: " + testResult.getName());
        }

        // Close the browser and clean up the WebDriver instance
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); // Clean up the ThreadLocal driver instance
        }
    }
    @AfterClass
    public void tearDown() {
	// Close the WebDriver after each test
	if (getDriver() != null) {
	    getDriver().quit();
	    driver.remove();
	}
    }
}
