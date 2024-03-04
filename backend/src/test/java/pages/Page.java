package pages;
import org.openqa.selenium.WebDriver;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public abstract class Page {
    WebDriver driver;

    public Page(WebDriver driver){
        this.driver = driver;
    }
}
