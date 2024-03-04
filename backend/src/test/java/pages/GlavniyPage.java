package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class GlavniyPage extends Page {
    private final String handle;
    @FindBy(xpath = "//button[text()='Распределить роли и начать игру']")
    private WebElement distributeRolesButton;
    @FindBy(xpath = "//button[@id='yesBtn']")
    private WebElement acceptCriterionButton;
    @FindBy(xpath = "//p[@id='manager_message']")
    private WebElement managerMessage;
    public GlavniyPage(WebDriver driver, String handle) {
        super(driver);
        this.handle = handle;
    }

    public void distributeRoles() {
        driver.switchTo().window(handle);
        distributeRolesButton.click();
    }

    public boolean isDistributeRolesButtonVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//button[text()='Распределить роли и начать игру']");
    }

    public void acceptCriterion() {
        driver.switchTo().window(handle);
        acceptCriterionButton.click();
    }

    public boolean isAcceptCriterionButtonVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//button[@id='yesBtn']");
    }

    public String getManagerMessage() {
        driver.switchTo().window(handle);
        return managerMessage.getText();
    }

    public boolean isManagerMessageVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//p[@id='manager_message']");
    }
}
