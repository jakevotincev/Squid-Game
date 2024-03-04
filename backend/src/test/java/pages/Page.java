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

    public Page(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    protected boolean checkElementVisible(String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(xpath))).isDisplayed();
    }
}
