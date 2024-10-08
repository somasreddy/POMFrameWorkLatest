package pom.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pom.util.BaseUtil;

public class HomePage extends BaseUtil {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // Constructor to initialize WebElements using PageFactory
    public HomePage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Page Factory - Define WebElements
    @FindBy(xpath = "//span[contains(text(),'Products')]")
    private WebElement productsList;

    @FindBy(xpath = "//div[@class='inventory_item'][1]//a/div")  
    private WebElement firstItemName;

    @FindBy(xpath = "//div[@class='inventory_item'][1]//div[@class='inventory_item_price']") 
    private WebElement firstItemPrice;

    @FindBy(xpath = "//div[1]/div/div/div[@class='inventory_item_price']/../button") 
    private WebElement addToCartBtn;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")  
    private WebElement cartIcon;

    @FindBy(xpath = "//div[@class='cart_item_label']/a/div")  
    private WebElement itemNameInCart;

    @FindBy(xpath = "//div[@class='cart_item_label']/div/div")  
    private WebElement itemPriceInCart;

    @FindBy(id = "react-burger-menu-btn")  
    private WebElement optnIcon;

    @FindBy(id = "logout_sidebar_link")  
    private WebElement logOutLink;

    // Method to verify if the homepage is displayed
    public boolean verifyHomePage() {
        try {
            boolean isDisplayed = productsList.isDisplayed();
            logger.info("HomePage displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error verifying HomePage: " + e.getMessage());
            return false;
        }
    }

    // Method to get the name of the first item
    public String getFirstItemName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(firstItemName));  // Added wait
            String name = firstItemName.getText().toString();
            logger.info("Retrieved first item name: " + name);
            return name;
        } catch (Exception e) {
            logger.error("Error retrieving first item name: " + e.getMessage());
            return "Unknown Item";  // Fallback value in case of failure
        }
    }

    // Method to get the price of the first item
    public String getFirstItemPrice() {
        try {
            wait.until(ExpectedConditions.visibilityOf(firstItemPrice));  // Added wait
            String price = firstItemPrice.getText().toString();
            logger.info("Retrieved first item price: " + price);
            return price;
        } catch (Exception e) {
            logger.error("Error retrieving first item price: " + e.getMessage());
            return "Unknown Price";  // Fallback value in case of failure
        }
    }
    
    public void addItemToCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));  // Wait until the button is clickable
            addToCartBtn.click();
            logger.info("Item added to cart successfully.");
        } catch (Exception e) {
            logger.error("Error adding item to cart: " + e.getMessage());
        }
    }
    
    public void navigateToCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartIcon));  // Wait until the cart icon is clickable
            cartIcon.click();
            logger.info("Navigated to cart successfully.");
        } catch (Exception e) {
            logger.error("Error navigating to cart: " + e.getMessage());
        }
    }
    
    public String getCartItemName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(itemNameInCart));  // Added wait
            return itemNameInCart.getText().toString();
        } catch (Exception e) {
            logger.error("Error retrieving item name from cart: " + e.getMessage());
            return "Unknown Item";  // Fallback value
        }
    }
    
    public String getCartItemPrice() {
        try {
            wait.until(ExpectedConditions.visibilityOf(itemPriceInCart));  // Added wait
            return itemPriceInCart.getText().toString();
        } catch (Exception e) {
            logger.error("Error retrieving item price from cart: " + e.getMessage());
            return "Unknown Price";  // Fallback value
        }
    }
    
    public boolean verifyItemNameInCart() {
        String itemNameInCart = getCartItemName();
        String firstItemName=fileProp.getProperty("Item_Name");
        boolean isNameCorrect = itemNameInCart.contains(firstItemName);
        logger.info("Verifying item name in cart: " + isNameCorrect);
        logger.info("Expected: " + firstItemName + ", Actual: " + itemNameInCart);
        return isNameCorrect;
    }
    
    public boolean verifyItemPriceInCart() {
        String itemPriceInCart = getCartItemPrice();
        String firstItemPrice = fileProp.getProperty("Item_Price");
        boolean isPriceCorrect = itemPriceInCart.contains(firstItemPrice);
        logger.info("Verifying item price in cart: " + isPriceCorrect);
        logger.info("Expected: " + firstItemPrice + ", Actual: " + itemPriceInCart);
        return isPriceCorrect;
    }
    
    public void logOut() {
        try {
            optnIcon.click();
            wait.until(ExpectedConditions.elementToBeClickable(logOutLink));
            logOutLink.click();
            logger.info("Logged out successfully.");
        } catch (Exception e) {
            logger.error("Error during logout: " + e.getMessage());
        }
    }
}
