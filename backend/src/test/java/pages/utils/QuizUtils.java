package pages.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author evotintsev
 * @since 05.03.2024
 */
public class QuizUtils {

    public static void answerQuestions(WebDriver driver, List<String> answers, boolean isInput) {
        AtomicInteger questionNumber = new AtomicInteger(1);
        answers.forEach(answer -> {
            if (isInput) {
                QuizUtils.sendInputAnswer(driver, answer, questionNumber);
                questionNumber.getAndIncrement();
            } else {
                QuizUtils.sendAnswer(driver, answer, questionNumber);
                questionNumber.getAndIncrement();
            }
        });

    }

    private static void sendAnswer(WebDriver driver, String answer, AtomicInteger questionNumber) {
        try {
            WebElement input = driver.findElement(By.xpath("//input[@value='" + answer + "']"));
            input.click();
            WebElement sendAnswerButton = driver.findElement(
                    By.xpath("(//button[text()='Отправить ответ'])[" + questionNumber + "]"));
            sendAnswerButton.click();
        } catch (StaleElementReferenceException e){
            //todo: if not found nothing to stop sending
        }
    }

    private static void sendInputAnswer(WebDriver driver, String answer, AtomicInteger questionNumber) {
        WebElement input = driver.findElement(By.xpath("(//input)[" + questionNumber + "]"));
        input.sendKeys(answer);

        WebElement sendAnswerButton = driver.findElement(
                By.xpath("(//button[text()='Отправить ответ'])[" + questionNumber + "]"));
        sendAnswerButton.click();
    }
}
