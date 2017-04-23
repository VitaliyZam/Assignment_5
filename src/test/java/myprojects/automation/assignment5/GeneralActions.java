package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private ProductData product;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverWait explicitWait() {
        return (WebDriverWait) new WebDriverWait(this.driver, 30).ignoring(StaleElementReferenceException.class);
    }

    public WebElement waitElementVisibility(By locator) {
        return explicitWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public WebElement waitElementPresence(By locator) {
        return explicitWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    public WebElement waitElementToBeClickable(By locator) {
        return explicitWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public ExpectedCondition<Boolean> jQueryAJAXCallsAreCompleted() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public void waitForPageLoad() {
        explicitWait().until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        });
    }

    public void goToMainPage() {
        driver.navigate().to(Properties.getBaseUrl());
        waitForPageLoad();
    }

    public boolean isMobileSite() {
        WebElement mobileElement = driver.findElement(By.xpath("//*[@class='hidden-md-up text-xs-center mobile']"));
        return mobileElement.isDisplayed();
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        waitElementPresence(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']/i")).click();
        List<WebElement> articles = new ArrayList<>(explicitWait().until(ExpectedConditions.
                visibilityOfAllElementsLocatedBy(By.xpath("//div/h1/a"))));
        Random random = new Random();
        articles.get(random.nextInt(articles.size())).click();
        waitForPageLoad();
        //throw new UnsupportedOperationException();
    }

    public void saveProductParameters() {
        String productName = driver.findElement(By.xpath("//h1[@itemprop]")).getText().trim().toLowerCase();
        String qty = driver.findElement(By.xpath("//input[@id='quantity_wanted']")).getAttribute("value").trim();
        String price = driver.findElement(By.xpath("//span[@itemprop='price']")).getText().trim();
        System.out.println(productName + " " + qty + " " + price + " - " + DataConverter.parsePriceValue(price));
        product = new ProductData(productName, DataConverter.parseStockValue(qty), DataConverter.parsePriceValue(price));
    }

    public void addToCart() {
        waitElementToBeClickable(By.xpath("//button[@class='btn btn-primary add-to-cart']")).click();
        waitElementVisibility(By.xpath("//div[@id='blockcart-modal']//div[@class='modal-body']"));
    }

    public void goToCart() {
        waitElementToBeClickable(By.xpath("//a[@class='btn btn-primary']")).click();
        waitElementVisibility(By.xpath("//section/div[@class='container']"));
    }

    public void validateProductInfo() {
        System.out.println(driver.findElement(By.xpath("//span[@class='label js-subtotal']")).getText());
        String subTotalQty = extractNumber(driver.findElement(By.xpath("//span[@class='label js-subtotal']")).getText());
        Assert.assertTrue(subTotalQty.equals("1"), "Wrong number of items is added to cart");
        String productNameCompare = driver.findElement(By.xpath("//div[@class='product-line-info']/a")).getText().toLowerCase().trim();
        Assert.assertTrue(productNameCompare.equals(product.getName()), "Wrong product name detected in the cart");
        String comparingPrice = driver.findElement(By.tagName("strong")).getText().trim();
        System.out.println(comparingPrice);
        Assert.assertTrue(DataConverter.parsePriceValue(comparingPrice) == product.getPrice(), "Product price in the cart doesn't correspond to the price on the product page");
    }

    public void proceedToOrderCreation() {
        waitElementToBeClickable(By.xpath("//a[@class='btn btn-primary']")).click();
    }

    public void fillTheForm() {
        
    }

    public String extractNumber(String value) {
        String result = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ' ') break;
            result += value.charAt(i);
        }
        return result;
    }


    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        throw new UnsupportedOperationException();
    }
}
