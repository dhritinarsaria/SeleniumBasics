package browser.testing;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import wrappers.Waits;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private final Waits waits;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    private By cookieAcceptBtn = By.cssSelector("#onetrust-accept-btn-handler, button[aria-label='Accept all']");
    private By primaryNavLinks = By.cssSelector("a[href]");
    private By heroHeading = By.cssSelector("h1, h2");

    public void open(String url) {
        driver.get(url);
        waits.pageIsReady(20);
        acceptCookiesIfPresent();
    }

    public void acceptCookiesIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(ExpectedConditions.presenceOfElementLocated(cookieAcceptBtn));
            WebElement btn = driver.findElement(cookieAcceptBtn);
            if (btn.isDisplayed()) {
                btn.click();
            }
        } catch (TimeoutException | NoSuchElementException ignored) {
            // banner not present
        }
    }

    /** Example: Return all visible nav links */
    public List<WebElement> visibleLinks() {
        List<WebElement> links = driver.findElements(primaryNavLinks);
        return links.stream().filter(WebElement::isDisplayed).toList();
    }

    public String pageTitle() {
        return driver.getTitle();
    }

    public WebElement firstHeroHeading() {
        return waits.visible(heroHeading, 15);
    }

    /** Example interaction: scroll to bottom to trigger lazy-load content (AJAX) and wait for settle */
    public void scrollToBottomAndWaitAjax() {
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        waits.ajaxComplete(20);
    }

    /** Safely click a link by partial text with explicit wait */
    public void clickLinkByPartialText(String partial) {
        By locator = By.partialLinkText(partial);
        waits.clickable(locator, 15).click();
    }
}
