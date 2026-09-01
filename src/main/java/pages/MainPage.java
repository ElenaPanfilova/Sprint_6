package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // === ЛОКАТОРЫ ===

    // Кнопка "Заказать" вверху страницы
    private final By topOrderButton = By.xpath("//button[text()='Заказать']");

    // Кнопка "Заказать" внизу страницы
    private final By bottomOrderButton = By.xpath("//div[contains(@class, 'Home_FinishButton__1_cWm')]//button[text()='Заказать']");

    // Блок с вопросами "Вопросы о важном"
    private final By faqBlock = By.className("Home_FAQ__3uVm4");

    // Кнопка принятия cookie
    private final By cookieButton = By.xpath("//button[text()='Да все привыкли']");

    // Заголовок страницы (для проверки загрузки)
    private final By pageTitle = By.className("Home_Header__iJKdX");

    // === КОНСТРУКТОР ===

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // === МЕТОДЫ ===

    // Принять cookie
    public void acceptCookies() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
        } catch (Exception e) {
            // Баннера нет — игнорируем
        }
    }

    // Ожидание загрузки главной страницы
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    // Клик по кнопке "Заказать" вверху
    public void clickTopOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
    }

    // Клик по кнопке "Заказать" внизу
    public void clickBottomOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
        scrollToElement(button);
        button.click();
    }

    // Скролл до блока FAQ
    public void scrollToFaqBlock() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(faqBlock));
        scrollToElement(element);
    }

    // Клик по вопросам с ожиданием появления ответов
    public void clickFaqQuestion(int index) {
        By questionLocator = By.id("accordion__heading-" + index);
        WebElement question = wait.until(ExpectedConditions.elementToBeClickable(questionLocator));
        scrollToElement(question);
        question.click();

        By answerLocator = By.xpath("//div[@id='accordion__panel-" + index + "']/p");
        wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));
    }

    // Получить текст ответа на вопрос
    public String getFaqAnswer(int index) {
        By answerLocator = By.xpath("//div[@id='accordion__panel-" + index + "']/p");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator)).getText();
    }

    // Проверить, отображается ли ответ на вопрос
    public boolean isFaqAnswerDisplayed(int index) {
        try {
            By answerLocator = By.xpath("//div[@id='accordion__panel-" + index + "']/p");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Скролл до элемента
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
