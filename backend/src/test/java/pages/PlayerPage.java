package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class PlayerPage extends Page {
    private final String handle;
    private final String username;
    @FindBy(xpath = "//input[@id='anketa']")
    private WebElement formInput;
    @FindBy(xpath = "//button[text()='Отправить']")
    private WebElement sendFormButton;

    public PlayerPage(WebDriver driver, String handle, String username) {
        super(driver);
        this.handle = handle;
        this.username = username;
    }

    public void sendForm(String form) {
        driver.switchTo().window(handle);
        formInput.sendKeys(form);
        sendFormButton.click();
    }

    public boolean isFormInputVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//input[@id='anketa']");
    }
}
