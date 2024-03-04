package pages;

import org.openqa.selenium.WebDriver;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class GlavniyPage extends Page {
    private final String handle;
    public GlavniyPage(WebDriver driver, String handle) {
        super(driver);
        this.handle = handle;
    }
}
