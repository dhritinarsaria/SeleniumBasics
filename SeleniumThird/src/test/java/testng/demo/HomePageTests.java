package testng.demo;



import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import browser.testing.HomePage;
import wrappers.AssertionsUtil;
import wrappers.DriverFactory;
import wrappers.Waits;

import java.util.List;
import java.util.stream.Collectors;

public class HomePageTests {

    private WebDriver driver;
    private Waits waits;
    private HomePage home;

    @Parameters({"baseUrl", "implicitWaitSec"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("https://www.sc.com/en/") String baseUrl,
                      @Optional("5") String implicitWaitSec,
                      ITestContext context) {
        DriverFactory.initDriver(System.getProperty("selenium.browser", "chrome"),
                Long.parseLong(implicitWaitSec));
        driver = DriverFactory.getDriver();
        waits = new Waits(driver);
        home = new HomePage(driver);
        home.open(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /** Why synchronization? Demonstrate flaky behavior solved by waits */
    @Test
    public void t01_whySynchronization() throws Exception {
        // Bad example (flaky): force a pause, not tied to UI state
        Thread.sleep(1000);

        // Good: wait for page ready and hero heading visible
        boolean ready = waits.pageIsReady(20);
        Assert.assertTrue(ready, "Page did not reach ready state in time");
        WebElement hero = home.firstHeroHeading();
        AssertionsUtil.assertDisplayed(hero, "Hero heading should be visible");
    }

    /** Implicit wait is globally set in DriverFactory; explicit waits for critical actions. */
    @Test
    public void t02_explicitWait_clickabilityAndText() {
        // Wait for first hero heading (explicit wait inside page object)
        WebElement hero = home.firstHeroHeading();
        AssertionsUtil.assertDisplayed(hero, "Hero heading visible");

        // Verify title contains "Standard Chartered" (example; title may vary per locale/CDN)
        String title = home.pageTitle();
        Assert.assertTrue(title.toLowerCase().contains("standard chartered"),
                "Title should contain bank name. Actual: " + title);
    }

    // Handling Ajax applications by scrolling and waiting for network to settle */
    @Test
    public void t03_ajaxHandling_scrollAndWait() {
        home.scrollToBottomAndWaitAjax();
        // After AJAX settles, inspect links again
        List<WebElement> links = home.visibleLinks();
        Assert.assertTrue(links.size() > 10, "Expected many visible links after lazy-load");
    }

    /** Verification: element properties, clickable buttons/links */
    @Test
    public void t04_verifyElementsAndLinks() {
        List<WebElement> links = home.visibleLinks();
        // Ensure links have href and are enabled
        for (WebElement a : links.stream().limit(20).collect(Collectors.toList())) {
            AssertionsUtil.assertDisplayed(a, "Link should be displayed");
            AssertionsUtil.assertEnabled(a, "Link should be enabled");
            Assert.assertNotNull(a.getAttribute("href"), "href must be present");
        }
    }

    /** Example of clicking a link safely using explicit wait */
    @Test
    public void t05_clickALinkSafely() {
        home.clickLinkByPartialText("About"); // If text changes, update partial
        boolean ok = waits.urlContains("about", 10);
        Assert.assertTrue(ok, "URL should contain 'about' after clicking About link");
    }

    /** Assertions examples: attributes and CSS */
    @Test
    public void t06_assertions_attributesAndCss() {
        WebElement hero = home.firstHeroHeading();
        // Attribute existence check
        String cls = hero.getAttribute("class");
        Assert.assertNotNull(cls, "Hero heading should have a class attribute");

        // CSS verification example (font-weight existence)
        String fw = hero.getCssValue("font-weight");
        Assert.assertTrue(fw != null && !fw.isEmpty(), "Font weight should be present");
    }

    /** Demonstrate Thread.sleep usage (discouraged) only for teaching */
    @Test
    public void t07_threadSleepDemo() throws InterruptedException {
        // Avoid, but shown for completeness
        Thread.sleep(500);
        Assert.assertTrue(((JavascriptExecutor)driver)
                .executeScript("return document.readyState").equals("complete"));
    }
}

