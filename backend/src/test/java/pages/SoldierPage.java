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
    @FindBy(xpath = "//div[@id='preys']")
    private WebElement preys;
    @FindBy(xpath = "//button[text()='Сделать выстрел']")
    private WebElement shootButton;
    @FindBy(xpath = "//label[@id='shoot_score']")
    private WebElement shootScore;
    @FindBy(xpath = "//h4")
    private WebElement shootResultMessage;
    @FindBy(xpath = "//table")
    private WebElement resultsTable;

    public SoldierPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    public boolean isClickerButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[@class='ClickerBtn']");
    }

    public void click(int score) {
        driver.switchTo().window(getHandle());
        for (int i = 0; i < score; i++) {
            clickerButton.click();
        }
    }

    public boolean checkClickerResult(int score) {
        driver.switchTo().window(getHandle());
        return clickerResult.getText().contains(String.valueOf(score));
    }

    public boolean isPreysVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[@id='preys']");
    }

    public boolean isContainsPrayName(String name) {
        return preys.getText().contains(name);
    }

    public void shoot() {
        driver.switchTo().window(getHandle());
        shootButton.click();
    }

    public boolean isShootButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[text()='Сделать выстрел']");
    }

    public boolean isShootScoreVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//label[@id='shoot_score']");
    }

    public int getShootScore() {
        driver.switchTo().window(getHandle());
        return Integer.parseInt(shootScore.getText());
    }

    public boolean isShootResultMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//h4");
    }

    public boolean isKilled() {
        driver.switchTo().window(getHandle());
        return shootResultMessage.getText().contains("Вы убили");
    }

    public boolean isMissed() {
        driver.switchTo().window(getHandle());
        return shootResultMessage.getText().contains("Вы промахнулись");
    }

    public boolean isResultsTableVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//table");
    }
}
