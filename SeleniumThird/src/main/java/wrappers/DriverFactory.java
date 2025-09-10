package wrappers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;


public class DriverFactory {

	private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	
	public static void initDriver(String browser, long implicitWaitSec) {
        if (browser == null) browser = System.getProperty("selenium.browser", "chrome");
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions ff = new FirefoxOptions();
                ff.addArguments("-width=1400","-height=900");
                driver = new FirefoxDriver(ff);
                break;
            case "edge":
                EdgeOptions eo = new EdgeOptions();
                driver = new EdgeDriver(eo);
                break;
            case "chrome":
            default:
                ChromeOptions co = new ChromeOptions();
                co.addArguments("--start-maximized");
                co.addArguments("--disable-notifications");
                co.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(co);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSec));
        tlDriver.set(driver);

	
}
}

