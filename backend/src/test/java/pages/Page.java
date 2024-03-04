package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public abstract class Page {
    WebDriver driver;
    private final String handle;
    private final String username;

    public Page(WebDriver driver, String handle, String username){
        this.driver = driver;
        this.handle = handle;
        this.username = username;
        PageFactory.initElements(this.driver, this);
    }

    protected boolean checkElementVisible(String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(xpath))).isDisplayed();
    }

    public String getHandle() {
        return handle;
    }

    public String getUsername() {
        return username;
    }
}
