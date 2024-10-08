package pom.qa.testCases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pom.pages.LoginPage;
import pom.util.BaseUtil;

public class LoginPageTest extends BaseUtil {
    private LoginPage login;

    // Setup method to initialize the browser and page objects
    @BeforeMethod
    public void startSetUp() {
	BrowserInitialize();
	login = new LoginPage();
    }

    // Test to verify the logo on the login page
    @Test
    public void verifyLoginPageLogo() {
	Assert.assertTrue(login.verifyLogo(), "Logo is not displayed on the login page.");
    }

    // Test to verify successful login
    @Test(dependsOnMethods = "verifyLoginPageLogo")
    public void verifyLogin() {
	login.loginToApp(setUProp.getProperty("username"), setUProp.getProperty("password"));
	Assert.assertEquals(getPageTitle(), setUProp.getProperty("loginTitle"),
		"Page title does not match expected title.");
    }

    // Cleanup method to quit the browser after tests
    @AfterMethod
    public void tearDown() {
	if (getDriver() != null) {
	    getDriver().quit();
	}
    }
}
