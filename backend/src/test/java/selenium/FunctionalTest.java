package selenium;

import config.ConfProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import utils.CredentialsReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class FunctionalTest {
    public static LoginPage loginPage;
    private GlavniyPage glavniyPage;
    private ManagerPage managerPage;
    private final List<UndefinedPage> undefinedPages = new ArrayList<>();
    private final Map<String, PlayerPage> playerPages = new HashMap<>();
    private final Map<WorkerPage, String> workerPages = new HashMap<>();
    private final Map<SoldierPage, String> soldierPages = new HashMap<>();
    public WebDriver webDriver;
    private Process process;
    private final String jarPath = "target/backend-0.0.1-SNAPSHOT.jar";
    private final String startUpMessage = "Started BackendApplication in";

    private volatile boolean isServerStart = false;
    //todo: add automatic frontend start?

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        if (Boolean.parseBoolean(ConfProperties.getProperty("start.server"))) {
            startServer();
        }
        if (Boolean.parseBoolean(ConfProperties.getProperty("run.chrome")))
            if (ConfProperties.getProperty("webdriver.chrome.driver") != null && ConfProperties.getProperty("chromedriver") != null) {
                System.setProperty(ConfProperties.getProperty("webdriver.chrome.driver"), ConfProperties.getProperty("chromedriver"));
                webDriver = new ChromeDriver();
            }
    }

    //todo: add tests for interrupts
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
        Iterator<Map.Entry<String, PlayerPage>> iterator = playerPages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PlayerPage> entry = iterator.next();
            String form = entry.getKey();
            PlayerPage page = entry.getValue();

            if (acceptedForms.contains(form)) {
                Assertions.assertTrue(page.isPassedSelectionMessageVisible());
            } else {
                Assertions.assertTrue(page.isFailedSelectionMessageVisible());

                page.close();
                iterator.remove();
            }
        }

        //start lunch
        Assertions.assertTrue(managerPage.isStartLunchButtonVisible());
        managerPage.startLunch();

        //answer lunch questions
        //todo: добавить не ол коррект квесчонс
        workerPages.forEach((page, username) -> {
            page.answerFoodQuestions(true);
        });

        //answer player lunch questions
        //todo: добавить не ол коррект квесчонс
        playerPages.forEach((form, page) -> {
            page.answerFoodQuestions(true);
        });

        //start prepare round
        Assertions.assertTrue(managerPage.isStartPrepareRoundButtonVisible());
        managerPage.startPrepareRound();

        //answer prepare round questions
        //todo: добавить не ол коррект квесчонс
        workerPages.forEach((page, username) -> {
            page.answerPrepareRoundQuestions(true);
        });

        // start training
        Assertions.assertTrue(managerPage.isStartTrainingButtonVisible());
        managerPage.startTraining();

        // train
        final int[] score = {5};

        soldierPages.forEach((page, username) -> {
            Assertions.assertTrue(page.isClickerButtonVisible());
            page.click(score[0]);
            page.checkClickerResult(score[0]);
            score[0] = score[0] + 5;
        });

        // stop training
        Assertions.assertTrue(managerPage.isStopTrainingButtonVisible());
        managerPage.stopTraining();

        //start game
        Assertions.assertTrue(glavniyPage.isStartGameButtonVisible());
        glavniyPage.startGame();

        //play game
        final boolean[] allCorrect = {true};
        AtomicReference<String> preyName = new AtomicReference<>("");
        playerPages.forEach((form, page) -> {
            page.answerGameQuestions(allCorrect[0]);

            if (allCorrect[0]) {
                page.isQualifiedMessageVisible();
            } else {
                preyName.set(page.getUsername());

                //check kill message visible
                Assertions.assertTrue(page.isWrongAnswerMessageVisible());
            }

            allCorrect[0] = !allCorrect[0];
        });


        score[0] = score[0] - 10;
        List<Integer> scores = new ArrayList<>();
        //check prey name and shoot
        soldierPages.forEach((page, username) -> {
            Assertions.assertTrue(page.isPreysVisible());
            Assertions.assertTrue(page.isContainsPrayName(preyName.get()));

            Assertions.assertTrue(page.isShootButtonVisible());
            page.isShootScoreVisible();
            scores.add(page.getShootScore() + score[0]);
            score[0] = score[0] + 5;
            page.shoot();
        });

        //check killed
        //todo: бывают ошибки
        soldierPages.forEach((page, username) -> {
            Assertions.assertTrue(page.isShootResultMessageVisible());
            if (scores.get(0).equals(scores.get(1))) {
                return;
            } else if (scores.get(0) > scores.get(1)) {
                Assertions.assertTrue(page.isKilled());
            } else {
                Assertions.assertTrue(page.isMissed());
            }
            //:D
            int saveScore = scores.get(0);
            scores.remove(0);
            scores.add(saveScore);
        });

        //check killed message
        String prayForm = "form".concat("-").concat(preyName.get());
        PlayerPage prayPage = playerPages.get(prayForm);
        Assertions.assertTrue(prayPage.isKilledMessageVisible());
        prayPage.close();
        playerPages.remove(prayForm);
    }

    @AfterEach
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
        stopServer();
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

    private void startServer() throws IOException, InterruptedException {
        if (process == null || !process.isAlive()) {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath);
            process = processBuilder.start();


            Thread waitServerStartThread = new Thread(() -> {
                try {
                    waitForServerToStart(process.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            waitServerStartThread.start();
            while (!isServerStart) {
                Thread.onSpinWait();
            }

            System.out.println("Server started.");
        } else {
            System.out.println("Server stopped");
        }
    }

    private void waitForServerToStart(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Выводим все логи в консоль
            if (line.contains(startUpMessage)) {
                // Обнаружено сообщение о запуске
                isServerStart = true;
            }
        }
    }

    public void stopServer() {
        if (process != null && process.isAlive()) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                process.destroyForcibly();
            }
            System.out.println("Server stopped.");
        } else {
            System.out.println("Server is not running.");
        }
    }
}
