package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class GlavniyPage extends Page {
    @FindBy(xpath = "//button[text()='Распределить роли и начать игру']")
    private WebElement distributeRolesButton;
    @FindBy(xpath = "//button[@id='yesBtn']")
    private WebElement acceptCriterionButton;
    @FindBy(xpath = "//p[@id='manager_message']")
    private WebElement managerMessage;
    public GlavniyPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    public void distributeRoles() {
        driver.switchTo().window(getHandle());
        distributeRolesButton.click();
    }

    public boolean isDistributeRolesButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Распределить роли и начать игру']");
    }

    public void acceptCriterion() {
        driver.switchTo().window(getHandle());
        acceptCriterionButton.click();
    }

    public boolean isAcceptCriterionButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[@id='yesBtn']");
    }

    public String getManagerMessage() {
        driver.switchTo().window(getHandle());
        return managerMessage.getText();
    }

    public boolean isManagerMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//p[@id='manager_message']");
    }
}
