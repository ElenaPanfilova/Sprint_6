package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // === ПЕРВАЯ ФОРМА ===

    private final By firstNameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By lastNameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroStationInput = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[normalize-space()='Далее']");

    // === ВТОРАЯ ФОРМА ===

    private final By deliveryDateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodInput = By.className("Dropdown-placeholder");
    private final By blackColorCheckbox = By.id("black");
    private final By greyColorCheckbox = By.id("grey");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath(".//div[starts-with(@class, 'Order_Buttons')]/button[not(contains(@class,'Button_Inverted'))]");

    // === МОДАЛЬНОЕ ОКНО ===

    private final By confirmModal = By.xpath("//div[contains(@class,'Order_Modal')]");
    private final By confirmButton = By.xpath("//div[contains(@class,'Order_Modal')]//button[normalize-space()='Да']");
    private final By cancelButton = By.xpath("//div[contains(@class,'Order_Modal')]//button[normalize-space()='Нет']");

    // === ОКНО УСПЕШНОГО ЗАКАЗА ===

    private final By successMessage = By.xpath("//*[contains(text(),'Заказ оформлен')]");

    // === КОНСТРУКТОР ===

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // === ПЕРВАЯ ФОРМА ===

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
    }

    public void fillName(String name) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        element.clear();
        element.sendKeys(name);
    }

    public void fillLastName(String lastName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        element.clear();
        element.sendKeys(lastName);
    }

    public void fillAddress(String address) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput));
        element.clear();
        element.sendKeys(address);
    }

    public void fillMetroStation(String station) {
        WebElement metroInput = wait.until(ExpectedConditions.elementToBeClickable(metroStationInput));
        metroInput.click();
        metroInput.sendKeys(station);

        By stationOption = By.xpath("//div[contains(@class,'select-search__select')]//*[normalize-space()='" + station + "']");
        wait.until(ExpectedConditions.elementToBeClickable(stationOption)).click();
    }

    public void fillPhone(String phone) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        element.clear();
        element.sendKeys(phone);
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryDateInput));
    }

    // === ВТОРАЯ ФОРМА ===

    public void fillDeliveryDate(String date) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(deliveryDateInput));
        element.click();
        element.sendKeys(Keys.CONTROL, "a");
        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);
    }

    public void selectRentalPeriod(String period) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodInput));
        dropdown.click();

        By periodOption = By.xpath("//*[contains(@class,'Dropdown-menu')]//*[normalize-space()='" + period + "']");
        wait.until(ExpectedConditions.elementToBeClickable(periodOption)).click();
    }

    // Выбор цвета
    public void selectColor(String color) {
        if (color.equalsIgnoreCase("черный") || color.equalsIgnoreCase("чёрный")) {
            wait.until(ExpectedConditions.elementToBeClickable(blackColorCheckbox)).click();
        } else if (color.equalsIgnoreCase("серый")) {
            wait.until(ExpectedConditions.elementToBeClickable(greyColorCheckbox)).click();
        } else {
            throw new IllegalArgumentException("Неизвестный цвет: " + color);
        }
    }

    public void fillComment(String comment) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput));
        element.clear();
        element.sendKeys(comment);
    }

    // === ЗАКАЗ ===

    public void clickOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));

        // Скроллим до кнопки
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);

        // Кликаем
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();

        // Ждем появления окна подтверждения
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));
    }

    // === ПОДТВЕРЖДЕНИЕ ===

    public void clickConfirmButton() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
    }

    // === ПРОВЕРКА УСПЕШНОГО ЗАКАЗА ===

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }
}

