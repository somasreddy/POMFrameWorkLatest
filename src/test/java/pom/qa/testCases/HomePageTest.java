package pom.qa.testCases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pom.pages.HomePage;
import pom.pages.LoginPage;
import pom.util.BaseUtil;

public class HomePageTest extends BaseUtil {
    private LoginPage login;
    private HomePage home;

    @BeforeMethod
    public void startSetUp() {
	// Initialize browser and navigate to the login page
	BrowserInitialize();
	login = new LoginPage();
	// Log in using credentials from the config file and navigate to the home page
	login.loginToApp(setUProp.getProperty("username"), setUProp.getProperty("password"));
	home = login.getHomePage();
    }

    @Test
    public void verifyProductPage() {
	// Verify that the products page is displayed after login
	Assert.assertTrue(home.verifyHomePage(), "Products page is not displayed.");
    }

    @Test(dependsOnMethods = "verifyProductPage")
    public void getFirstItemNameAndPrice() {
	// Fetch item name and price
	String itemName = home.getFirstItemName();
	String itemPrice = home.getFirstItemPrice();

	// Assert that item name and price are not null
	Assert.assertNotNull(itemName, "Item name is null.");
	Assert.assertNotNull(itemPrice, "Item price is null.");

	// Write item details to file
	writeToFile(itemName, itemPrice);
    }

    @Test(dependsOnMethods = "getFirstItemNameAndPrice")
    public void verifyCartItem() {
	// Add item to cart
	home.addItemToCart();

	// Go to the cart and verify item name and price
	home.navigateToCart();
	Assert.assertTrue(home.verifyItemNameInCart(), "Item name in cart does not match the first item name.");
	Assert.assertTrue(home.verifyItemPriceInCart(), "Item price in cart does not match the first item price.");
    }

    @Test(dependsOnMethods = "verifyCartItem")
    public void verifyLogout() {
	home.logOut();
	Assert.assertTrue(login.verifyLogo(), "Logo is not displayed after logout.");
    }

    @AfterMethod
    public void tearDown() {
	// Close the WebDriver after each test
	if (getDriver() != null) {
	    getDriver().quit();
	}
    }
}
