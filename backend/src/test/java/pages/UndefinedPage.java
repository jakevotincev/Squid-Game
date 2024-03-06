package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class UndefinedPage extends Page {
    @FindBy(xpath = "//h1[@id='page_title']")
    private WebElement pageTitle;
    @FindBy(xpath = "//input[@class='inputButton' and @value='Login']")
    private WebElement loginButton;

    public UndefinedPage(WebDriver driver, String handle, String username) {
        super(driver, handle, username);
    }

    public Page getRolePage() {
        Role role = getRole();
        return switch (role) {
            case PLAYER -> new PlayerPage(driver, getHandle(), getUsername());
            case WORKER -> new WorkerPage(driver, getHandle(), getUsername());
            case SOLDIER -> new SoldierPage(driver, getHandle(), getUsername());
            default -> this;
        };
    }

    public Role getRole() {
        driver.switchTo().window(getHandle());
        String titleText;
        try {
            titleText = pageTitle.getText();
        } catch (NoSuchElementException e) {
            loginButton.click();
            titleText = pageTitle.getText();
        }
            if (titleText.contains("player")) {
                return Role.PLAYER;
            } else if (titleText.contains("worker")) {
                return Role.WORKER;
            } else if (titleText.contains("soldier")) {
                return Role.SOLDIER;
            } else {
                return Role.UNDEFINED;

            }
        }
    }
