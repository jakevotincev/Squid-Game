package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class WorkerPage extends Page {

    public WorkerPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    @FindBy(id = "//ul[@id='anketas_list']")
    private WebElement formList;
    @FindAll({@FindBy(xpath = "//li[@id='anketa']")})
    private List<WebElement> formInputs;
    @FindBy(xpath = "//button[text()='Завершить отбор анкет']")
    private WebElement acceptFormButton;

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

    public boolean isFormListVisible() {
        driver.switchTo().window(getHandle());
        return checkElementVisible("//ul[@id='anketas_list']");
    }
}
