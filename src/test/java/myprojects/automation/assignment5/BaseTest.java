package myprojects.automation.assignment5;

import myprojects.automation.assignment5.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Base script functionality, can be used for all Selenium scripts.
 */
public abstract class BaseTest {
    protected WebDriver driver;
    protected GeneralActions actions;
    protected boolean isMobileTesting;
    Process hub;
    Process node;

    /**
     * Prepares {@link WebDriver} instance with timeout and browser window configurations.
     * <p>
     * Driver type is based on passed parameters to the automation project,
     * creates {@link ChromeDriver} instance by default.
     */

    //@BeforeSuite
    public void setHubAndNodes() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String mainCommand = "java ";
            String jar = "-jar ";
            String seleniumServerJarPath = "\"D:/Lectures/selenium-server-standalone-3.4.0.jar\" ";
            String roleHub = "-role hub ";
            String port = "-port ";
            int portNum = 4447;
            String startHub = mainCommand + jar + seleniumServerJarPath + roleHub + port + portNum;
            hub = runtime.exec(startHub);
            String chromeSetPath = "-Dwebdriver.chrome.driver=\"D:\\Lectures\\chrome.exe\" ";
            String firefoxSetPath = "-Dwebdriver.gecko.driver=\"D:\\Lectures\\geckodriver.exe\" ";
            String ieSetPath = "-Dwebdriver.ie.driver=\"D:\\Lectures\\IEDriverServer.exe\" ";
            String phantomSetPath = "-Dphantomjs.binary.path=\"D:\\Lectures\\phantomjs.exe\" ";
            String roleNode = "-role node ";
            String regiser = "-hub http://localhost:" + portNum + "/grid/register ";
            String browser = "-browser ";
            String setInstFF = "\"browserName=firefox,maxInstances=2\" ";
            String setChromeInst = "\"browserName=chrome,maxInstances=2\" ";
            String setInstIE = "\"browserName=internet explorer,version=11,platform=WINDOWS,maxInstances=3\" ";
            String setInstPJS = "\"browserName=phantomjs,maxInstances=2\" ";
            String regisetNode = mainCommand + chromeSetPath + firefoxSetPath + ieSetPath + phantomSetPath + jar +
                    seleniumServerJarPath + roleNode + regiser + port + ++portNum + " " + browser + setChromeInst +
                    browser + setInstFF + browser + setInstIE + browser + setInstPJS;
            System.out.println(regisetNode);
            node = runtime.exec(regisetNode);
        } catch(Exception e) {
            destroyHubAndNodes();
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    @AfterSuite
    public void destroyHubAndNodes() {
        //node.destroyForcibly();
        //hub.destroyForcibly();
    }
    @BeforeClass
    @Parameters({"selenium.browser", "selenium.grid"})
    public void setUp(@Optional("chrome") String browser, @Optional("") String gridUrl) {
        // TODO create WebDriver instance according to passed parameters
        System.out.println(browser + " " + gridUrl);
        if (gridUrl.equals("")) {
            driver = DriverFactory.initDriver(browser);
            System.out.println("Regular testing");
        } else {
            driver = DriverFactory.initDriver(browser, gridUrl);
            System.out.println("Headless testing");
        }
        if (!isMobileTesting(browser))
            driver.manage().window().maximize();
        isMobileTesting = isMobileTesting(browser);
        actions = new GeneralActions(driver);
    }

    /**
     * Closes driver instance after test class execution.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     *
     * @return Whether required browser displays content in mobile mode.
     */
    private boolean isMobileTesting(String browser) {
        switch (browser) {
            case "android":
                return true;
            case "firefox":
            case "ie":
            case "internet explorer":
            case "chrome":
            case "phantomjs":
            default:
                return false;
        }
    }
}
