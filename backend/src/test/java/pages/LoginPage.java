package pages;

import config.ConfProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CredentialsReader;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class LoginPage extends Page {
    private final static String URL = "http://localhost:3000/Auth";
    private final Map<String, String> creds;
    @FindBy(xpath = "//input[contains(@placeholder, 'username')]")
    private WebElement loginInput;
    @FindBy(xpath = "//input[contains(@placeholder, 'password')]")
    private WebElement passwordInput;
    @FindBy(xpath = "//input[@class='inputButton' and @value='Login']")
    private WebElement loginButton;


    public LoginPage(WebDriver driver) {
        super(driver);
        //todo: нужно ли это?
        this.driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        this.driver.manage().window().maximize();

//        String currentHandle = this.driver.getWindowHandle();
//        Set<String> handles = this.driver.getWindowHandles();
        String credsPath = ConfProperties.getProperty("creds.path");
        creds = CredentialsReader.getCredentials(credsPath);
    }

    //todo: should login first
    public GlavniyPage loginAsGlavniy(String glavniyLogin) {
        this.driver.get(URL);
        GlavniyPage glavniyPage = new GlavniyPage(driver, driver.getWindowHandle());
        login(glavniyLogin);
        return glavniyPage;
    }

    public ManagerPage loginAsManager(String managerLogin) {
        driver.switchTo().newWindow(WindowType.TAB);
        this.driver.get(URL);
        ManagerPage managerPage = new ManagerPage(driver, driver.getWindowHandle());
        login(managerLogin);
        return managerPage;
    }

    public UndefinedPage loginAsUser(String userLogin) {
        driver.switchTo().newWindow(WindowType.TAB);
        this.driver.get(URL);
        UndefinedPage undefinedPage = new UndefinedPage(driver, driver.getWindowHandle(), userLogin);
        login(userLogin);
        return undefinedPage;
    }

    private void login(String username) {
        loginInput.sendKeys(username);
        passwordInput.sendKeys(creds.get(username));
        loginButton.click();
    }


}
