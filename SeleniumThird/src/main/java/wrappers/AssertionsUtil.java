package wrappers;



import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AssertionsUtil {

    public static void assertDisplayed(WebElement el, String msg) {
        Assert.assertTrue(el.isDisplayed(), msg);
    }

    public static void assertEnabled(WebElement el, String msg) {
        Assert.assertTrue(el.isEnabled(), msg);
    }

    public static void assertHasText(WebElement el, String expected, String msg) {
        Assert.assertEquals(el.getText().trim(), expected.trim(), msg);
    }

    public static void assertTitleEquals(org.openqa.selenium.WebDriver driver, String expected) {
        Assert.assertEquals(driver.getTitle().trim(), expected.trim(), "Page title mismatch");
    }

    public static void assertAttribute(WebElement el, String attr, String expected, String msg) {
        Assert.assertEquals(el.getAttribute(attr), expected, msg);
    }

    public static void assertCss(WebElement el, String prop, String expected, String msg) {
        Assert.assertEquals(el.getCssValue(prop), expected, msg);
    }
}
