package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class PlayerPage extends Page {
    private String form;
    @FindBy(xpath = "//input[@id='anketa']")
    private WebElement formInput;
    @FindBy(xpath = "//button[text()='Отправить']")
    private WebElement sendFormButton;

    public PlayerPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    public void sendForm() {
        driver.switchTo().window(getHandle());
        formInput.sendKeys(form);
        sendFormButton.click();
    }

    public boolean isFormInputVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//input[@id='anketa']");
    }

    public boolean isPassedSelectionMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[text()='Вы  прошли отбор для участия в игре']");
    }

    public boolean isFailedSelectionMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[text()='Вы не прошли отбор для участия']");
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
