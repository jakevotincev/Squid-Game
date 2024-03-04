package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class ManagerPage extends Page {
    private final String handle;
    @FindBy(xpath = "//input[@id='crit']")
    private WebElement criterionInput;
    @FindBy(xpath = "//input[@id='numb']")
    private WebElement playerNumberInput;
    @FindBy(xpath = "//button[text()='Отправить']")
    private WebElement sendCriterionButton;
    @FindBy(xpath = "//button[@id='boss_msg_btn']")
    private WebElement seeBossMessageButton;
    @FindBy(xpath = "//p[@id='boss_message']")
    private WebElement bossMessage;

    public ManagerPage(WebDriver driver, String handle) {
        super(driver);
        this.handle = handle;

    }

    public void sendCriterion(String criterion, String playerNumber) {
        driver.switchTo().window(handle);
        criterionInput.sendKeys(criterion);
        playerNumberInput.sendKeys(playerNumber);
        sendCriterionButton.click();
    }

    public boolean isCriterionInputVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//input[@id='crit']");
    }

    public void expandBossMessage() {
        driver.switchTo().window(handle);
        seeBossMessageButton.click();
    }

    public String getBossMessage() {
        driver.switchTo().window(handle);
        return bossMessage.getText();
    }

    public boolean isBossMessageVisible() {
        driver.switchTo().window(handle);
        return checkElementVisible("//p[@id='boss_message']");
    }
}
