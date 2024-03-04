package pages;

import org.openqa.selenium.WebDriver;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class UndefinedPage extends Page {
    private final String handle;
    private final String username;
    public UndefinedPage(WebDriver driver, String handle, String username) {
        super(driver);
        this.handle = handle;
        this.username = username;
    }
}
