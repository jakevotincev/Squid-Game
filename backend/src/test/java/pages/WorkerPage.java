package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import pages.utils.QuizUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class WorkerPage extends Page {
    @FindBy(id = "//ul[@id='anketas_list']")
    private WebElement formList;
    @FindAll({@FindBy(xpath = "//li[@id='anketa']")})
    private List<WebElement> formInputs;
    @FindBy(xpath = "//button[text()='Завершить отбор анкет']")
    private WebElement acceptFormButton;
    @FindBy(xpath = "//button[@class='ClickerBtn']")
    private WebElement clickerButton;
    @FindBy(xpath = "//h4[contains(text(), 'Убрано органов')]")
    private WebElement clickerResult;
    @FindBy(xpath = "//table")
    private WebElement resultsTable;
    private List<String> allCorrectFoodAnswers = List.of(
            "Яйца",
            "Колбаса",
            "Капуста"
    );

    private List<String> oneIncorrectFoodAnswer = List.of(
            "Яйца",
            "Колбаса",
            "Огурец"
    );

    private List<String> allCorrectPrepareRoundAnswers = List.of(
            "11",
            "816",
            "116"
    );

    private List<String> oneIncorrectPrepareRoundAnswers = List.of(
            "11",
            "816",
            "1164"
    );

    public WorkerPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }


    public List<String> getForms() {
        List<String> forms = new ArrayList<>();
        driver.switchTo().window(getHandle());

        for (WebElement item : formInputs) {
            String playerInfo = item.getText();
            forms.add(playerInfo);
        }

        return forms;
    }

    public String acceptForm() {
        driver.switchTo().window(getHandle());
        WebElement checkbox = driver.findElement(By.xpath("(//input[@type='checkbox' and @value='Yes'])[1]"));

        WebElement parentListItem = checkbox.findElement(By.xpath("./ancestor::li"));

        String playerFormContent = parentListItem.getText().split(":")[2].split("\\s+")[1];
        checkbox.click();
        acceptFormButton.click();

        return playerFormContent;
    }

    public void answerFoodQuestions(boolean allCorrect) {
        driver.switchTo().window(getHandle());
        QuizUtils.answerQuestions(driver, allCorrect ? allCorrectFoodAnswers : oneIncorrectFoodAnswer, false);
    }

    public void answerPrepareRoundQuestions(boolean allCorrect) {
        driver.switchTo().window(getHandle());
        QuizUtils.answerQuestions(driver, allCorrect ? allCorrectPrepareRoundAnswers : oneIncorrectPrepareRoundAnswers, true);
    }

    public boolean isFormListVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//ul[@id='anketas_list']");
    }

    public void click(int score) {
        driver.switchTo().window(getHandle());
        for (int i = 0; i < score; i++) {
            clickerButton.click();
        }
    }

    public boolean isClickerButtonVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//button[@class='ClickerBtn']");
    }

    public boolean checkClickerResult(int score) {
        driver.switchTo().window(getHandle());
        return clickerResult.getText().contains(String.valueOf(score));
    }

    public boolean isResultsTableVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//table");
    }
}
