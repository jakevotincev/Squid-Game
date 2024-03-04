package selenium;

import config.ConfProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import ru.jakev.backend.entities.Role;
import utils.CredentialsReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class FunctionalTest {
    public static LoginPage loginPage;
    private GlavniyPage glavniyPage;
    private ManagerPage managerPage;
    private List<UndefinedPage> undefinedPages = new ArrayList<>();
    private List<PlayerPage> playerPages = new ArrayList<>();
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
    public void testBusinessCycle() {
        //login all
        loginPage = new LoginPage(webDriver);
        glavniyPage = loginPage.loginAsGlavniy("glavniy");
        managerPage = loginPage.loginAsManager("manager");
        List<String> creds = CredentialsReader.getLogins(ConfProperties.getProperty("creds.path"));
        creds.stream().skip(2).forEach(user -> {
            undefinedPages.add(loginPage.loginAsUser(user));
        });

        //distribute roles
        Assertions.assertTrue(glavniyPage.isDistributeRolesButtonVisible());
        glavniyPage.distributeRoles();

        //send criteria
        //todo: параметризовать все вводные данные
        Assertions.assertTrue(managerPage.isCriterionInputVisible());
        managerPage.sendCriterion("criteria", "2");

        //  check criteria
        Assertions.assertTrue(glavniyPage.isManagerMessageVisible());
        String managerMessage = glavniyPage.getManagerMessage();
        Assertions.assertTrue(managerMessage.contains("criteria") && managerMessage.contains("2"));

        //send criterion
        Assertions.assertTrue(glavniyPage.isAcceptCriterionButtonVisible());
        glavniyPage.acceptCriterion();

        //check boss message
        managerPage.expandBossMessage();
        Assertions.assertTrue(managerPage.isBossMessageVisible());
        String bossMessage = managerPage.getBossMessage();
        Assertions.assertTrue(bossMessage.contains("Критерии утверждены"));

        //extract player pages
        undefinedPages.stream().filter(page -> page.getRole().equals(Role.PLAYER)).forEach(page -> {
            playerPages.add((PlayerPage) page.getRolePage());
        });

        //send forms
        playerPages.forEach(page -> {
            Assertions.assertTrue(page.isFormInputVisible());
            page.sendForm("form");
        });
    }
}
