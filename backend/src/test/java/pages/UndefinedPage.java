package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class UndefinedPage extends Page {
    private final String handle;
    private final String username;
    @FindBy(xpath = "//h1[@id='page_title']")
    private WebElement pageTitle;

    public UndefinedPage(WebDriver driver, String handle, String username) {
        super(driver);
        this.handle = handle;
        this.username = username;
    }

    public Page getRolePage() {
        Role role = getRole();
        return switch (role) {
            case PLAYER -> new PlayerPage(driver, handle, username);
            case WORKER -> new WorkerPage(driver, handle, username);
            case SOLDIER -> new SoldierPage(driver, handle, username);
            default -> this;
        };
    }

    public Role getRole() {
        driver.switchTo().window(handle);
        String titleText = pageTitle.getText();
        if (titleText.contains("player")) {
            return Role.PLAYER;
        } else if (titleText.contains("worker")){
            return Role.WORKER;
        } else if (titleText.contains("soldier")){
            return Role.SOLDIER;
        } else {
            return Role.UNDEFINED;

        }
    }
}
