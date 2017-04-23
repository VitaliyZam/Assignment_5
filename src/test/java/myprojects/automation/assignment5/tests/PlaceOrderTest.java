package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.utils.DriverFactory;
import myprojects.automation.assignment5.utils.Properties;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        actions.goToMainPage();
        Assert.assertEquals(isMobileTesting, actions.isMobileSite(), "Inappropriate site version is open");
        System.out.println(DriverFactory.class.getResource("/IEDriverServer.exe").getPath());
    }

    @Test(dependsOnMethods = "checkSiteVersion")
    public void createNewOrder() {
        // TODO implement order creation test
        // open random product
        actions.openRandomProduct();

        // save product parameters
        actions.saveProductParameters();

        // add product to Cart and validate product information in the Cart
        actions.addToCart();
        actions.goToCart();
        actions.validateProductInfo();

        // proceed to order creation, fill required information
        actions.proceedToOrderCreation();

        // place new order and validate order summary

        // check updated In Stock value
    }

}
