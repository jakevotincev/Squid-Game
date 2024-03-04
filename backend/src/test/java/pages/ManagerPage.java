package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class ManagerPage extends Page {
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
    @FindBy(xpath = "//button[text()='Отправить анкеты рабочим']")
    private WebElement sendCriteriaToWorkersButton;
    @FindBy(xpath = "//button[text()='Отдать приказ о начале обеда']")
    private WebElement startLunchButton;
    @FindBy(xpath = "//button[text()='Отдать приказ о старте подготовки раунда']")
    private WebElement startPrepareRoundButton;
    @FindBy(xpath = "//button[text()='Отдать приказ о начале тренировок солдат']")
    private WebElement startTrainingButton;

    public ManagerPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);

    }

    public void sendCriterion(String criterion, String playerNumber) {
        driver.switchTo().window(getHandle());
        criterionInput.sendKeys(criterion);
        playerNumberInput.sendKeys(playerNumber);
        sendCriterionButton.click();
    }

    public boolean isCriterionInputVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//input[@id='crit']");
    }

    public void expandBossMessage() {
        driver.switchTo().window(getHandle());
        seeBossMessageButton.click();
    }

    public String getBossMessage() {
        driver.switchTo().window(getHandle());
        return bossMessage.getText();
    }

    public boolean isBossMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//p[@id='boss_message']");
    }

    public boolean isSendCriteriaToWorkersButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Отправить анкеты рабочим']");
    }

    public void sendCriteriaToWorkers() {
        driver.switchTo().window(getHandle());
        sendCriteriaToWorkersButton.click();
    }

    public boolean isStartLunchButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Отдать приказ о начале обеда']");
    }

    public boolean isStartPrepareRoundButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Отдать приказ о старте подготовки раунда']");
    }

    public void startPrepareRound() {
        driver.switchTo().window(getHandle());
        startPrepareRoundButton.click();
    }

    public void startLunch() {
        driver.switchTo().window(getHandle());
        startLunchButton.click();
    }

    public boolean isStartTrainingButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Отдать приказ о начале тренировок солдат']");
    }

    public void startTraining() {
        driver.switchTo().window(getHandle());
        startTrainingButton.click();
    }
}
