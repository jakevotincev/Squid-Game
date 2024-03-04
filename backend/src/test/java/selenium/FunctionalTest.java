package selenium;

import config.ConfProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import utils.CredentialsReader;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class FunctionalTest {
    public static LoginPage loginPage;
    private GlavniyPage glavniyPage;
    private ManagerPage managerPage;
    private List<UndefinedPage> undefinedPages = new ArrayList<>();
    private Map<String, PlayerPage> playerPages = new HashMap<>();
    private Map<WorkerPage, String> workerPages = new HashMap<>();
    private Map<SoldierPage, String> soldierPages = new HashMap<>();
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

        //extract pages
        extractPages();

        //send forms
        playerPages.forEach((form, page) -> {
            Assertions.assertTrue(page.isFormInputVisible());
            page.setForm(form);
            page.sendForm();
        });

        //send criteria to workers
        Assertions.assertTrue(managerPage.isSendCriteriaToWorkersButtonVisible());
        managerPage.sendCriteriaToWorkers();

        //check criteria, forms go to workers and acceptForms
        playerPages.forEach((form, page) -> {
            AtomicBoolean isFormSendToWorkers = new AtomicBoolean(false);
            workerPages.forEach((workerPage, username) -> {
                List<String> forms = workerPage.getForms();
                forms.forEach(f -> {
                    if (f.contains(form)) {
                        isFormSendToWorkers.set(true);
                    }
                });
            });

            Assertions.assertTrue(isFormSendToWorkers.get());
        });

        List<String> acceptedForms = new ArrayList<>();
        workerPages.forEach((page, username) -> {
            String acceptForm = page.acceptForm();
            acceptedForms.add(acceptForm);
        });

        //check if accepted players qualified and not accepted kicked
        playerPages.forEach((form, page) -> {
            if (acceptedForms.contains(form)) {
                Assertions.assertTrue(page.isPassedSelectionMessageVisible());
            } else {
                Assertions.assertTrue(page.isFailedSelectionMessageVisible());
            }
        });
    }

    private void extractPages() {
        Iterator<UndefinedPage> iterator = undefinedPages.iterator();
        while (iterator.hasNext()) {
            UndefinedPage page = iterator.next();

            switch (page.getRole()) {
                case PLAYER -> {
                    String playerForm = "form".concat("-").concat(page.getUsername());
                    playerPages.put(playerForm, (PlayerPage) page.getRolePage());
                }
                case WORKER -> {
                    workerPages.put((WorkerPage) page.getRolePage(), page.getUsername());
                }
                case SOLDIER -> {
                    soldierPages.put((SoldierPage) page.getRolePage(), page.getUsername());
                }
            }
            iterator.remove();

        }
    }
}
