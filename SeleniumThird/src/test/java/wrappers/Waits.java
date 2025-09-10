package wrappers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.function.Function;

public class Waits {

    private final WebDriver driver;

    public Waits(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement visible(By locator, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean urlContains(String fragment, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.urlContains(fragment));
    }

    /** Wait for page ready state to be 'complete' */
    public boolean pageIsReady(long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec)).until((ExpectedCondition<Boolean>) wd -> {
            Object result = ((JavascriptExecutor) wd).executeScript("return document.readyState");
            return "complete".equals(result);
        });
    }

    /** Generic AJAX wait: resolves true when jQuery + fetch/XHR network settles (best-effort). */
    public boolean ajaxComplete(long timeoutSec) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(JavascriptException.class);
        return wait.until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                try {
                    Object jq = ((JavascriptExecutor)d).executeScript("return window.jQuery ? jQuery.active : 0;");
                    long active = 0L;
                    if (jq instanceof Long) active = (Long) jq;
                    if (active > 0) return false;
                } catch (Exception ignore) {}
                // Check document.readyState as a secondary signal
                Object ready = ((JavascriptExecutor)d).executeScript("return document.readyState");
                return "complete".equals(ready);
            }
        });
    }

    /** Example of custom condition: text present in element */
    public boolean textPresent(By locator, String text, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
