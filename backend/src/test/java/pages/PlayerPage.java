package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.utils.QuizUtils;

import java.util.List;

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
    @FindBy(xpath = "//div[text()='Вы ошиблись, скоро вас убьют']")
    private WebElement wrongAnswerMessage;
    @FindBy(xpath = "//div[text()='Вы прошли в следующий раунд']")
    private WebElement qualifiedMessage;
    @FindBy(xpath = "//div[text()='ВАС убили, ВЫ проиграли']")
    private WebElement killedMessage;
    @FindBy(xpath = "//table")
    private WebElement resultsTable;

    private List<String> allCorrectFoodAnswers = List.of(
            "Борщ",
            "Оливье",
            "Блины"
    );

    private List<String> oneIncorrectFoodAnswer = List.of(
            "Борщ",
            "Оливье",
            "Медовик"
    );

    private List<String> allCorrectGameAnswers = List.of(
            "Рош ха-Шана",
            "0",
            "Драко Малфой"
    );

    private List<String> oneIncorrectGameAnswers = List.of(
            "Рош ха-Шана",
            "2",
            "Драко Малфой"
    );

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

    public void answerFoodQuestions(boolean allCorrect) {
        driver.switchTo().window(getHandle());
        QuizUtils.answerQuestions(driver, allCorrect ? allCorrectFoodAnswers : oneIncorrectFoodAnswer, false);
    }

    public void answerGameQuestions(boolean allCorrect) {
        driver.switchTo().window(getHandle());
        QuizUtils.answerQuestions(driver, allCorrect ? allCorrectGameAnswers : oneIncorrectGameAnswers, false);
    }

    public boolean isWrongAnswerMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[text()='Вы ошиблись, скоро вас убьют']");
    }

    public boolean isQualifiedMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[text()='Вы прошли в следующий раунд']");
    }

    public boolean isKilledMessageVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//div[text()='ВАС убили, ВЫ проиграли']");
    }

    public boolean isResultsTableVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//table");
    }
}
