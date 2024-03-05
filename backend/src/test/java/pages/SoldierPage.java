package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class SoldierPage extends Page {
    @FindBy(xpath = "//button[@class='ClickerBtn']")
    private WebElement clickerButton;

    @FindBy(xpath = "//h4[contains(text(), 'Сделано выстрелов')]")
    private WebElement clickerResult;

    public SoldierPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    public boolean isClickerButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[@class='ClickerBtn']");
    }

    public void click(int score) throws InterruptedException {
        driver.switchTo().window(getHandle());
        for (int i = 0; i < score; i++) {
            clickerButton.click();
            Thread.sleep(100);
        }
    }

    public boolean checkClickerResult(int score) {
        driver.switchTo().window(getHandle());
        return clickerResult.getText().contains(String.valueOf(score));
    }
}
