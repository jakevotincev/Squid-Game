package selenium;

import config.ConfProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.GlavniyPage;
import pages.LoginPage;
import pages.ManagerPage;
import pages.UndefinedPage;
import utils.CredentialsReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class TestSimple {
    public static LoginPage loginPage;
    private GlavniyPage glavniyPage;
    private ManagerPage managerPage;
    private List<UndefinedPage> undefinedPages = new ArrayList<>();
    public WebDriver webDriver;
    //todo: add automatic system start

    @BeforeEach
    void setUp() {
        if (Boolean.parseBoolean(ConfProperties.getProperty("run.chrome")))
            if (ConfProperties.getProperty("webdriver.chrome.driver") != null && ConfProperties.getProperty("chromedriver") != null) {
                System.setProperty(ConfProperties.getProperty("webdriver.chrome.driver"), ConfProperties.getProperty("chromedriver"));
                webDriver = new ChromeDriver();
            }
    }

    @Test
    public void test() {
        loginPage = new LoginPage(webDriver);
        glavniyPage = loginPage.loginAsGlavniy("glavniy");
        managerPage = loginPage.loginAsManager("manager");
        List<String> creds = CredentialsReader.getLogins(ConfProperties.getProperty("creds.path"));
        creds.stream().skip(2).forEach(user -> {
            undefinedPages.add(loginPage.loginAsUser(user));
        });

    }
}
