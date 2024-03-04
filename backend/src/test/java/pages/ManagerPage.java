package pages;

import org.openqa.selenium.WebDriver;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class ManagerPage extends Page{
    private final String handle;
    public ManagerPage(WebDriver driver, String handle) {
        super(driver);
        this.handle = handle;
    }
}
