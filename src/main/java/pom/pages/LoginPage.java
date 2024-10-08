package pom.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pom.util.BaseUtil;

public class LoginPage extends BaseUtil {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    // Constructor to initialize WebElements using PageFactory
    public LoginPage() {
	PageFactory.initElements(getDriver(), this);
    }

    // Page Factory - Define WebElements
    @FindBy(xpath = "//div[contains(text(),'Swag Labs')]")
    private WebElement swagLogo;

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    // Method to verify if Swag Labs logo is displayed
    public boolean verifyLogo() {
	try {
	    boolean isDisplayed = usernameField.isDisplayed();
	    logger.info("Swag Labs loginpage displayed: " + isDisplayed);
	    return isDisplayed;
	} catch (Exception e) {
	    logger.error("Error verifying loginPage: " + e.getMessage(), e);
	    return false;
	}
    }

    // Method to log in to the application
    public void loginToApp(String username, String password) {
	try {
	    logger.info("Attempting to log in with username: " + username);

	    // Wait until the username field is visible
	    wait.until(ExpectedConditions.visibilityOf(usernameField));

	    usernameField.sendKeys(username);
	    passwordField.sendKeys(password);
	    loginBtn.click();
	    logger.info("Login successful for user: " + username);
	} catch (Exception e) {
	    logger.error("Login failed for user: " + username + " - " + e.getMessage(), e);
	    throw e; // Rethrow exception for higher-level handling
	}
    }

    // Method to get the HomePage instance if needed separately
    public HomePage getHomePage() {
	return new HomePage();
    }
}
