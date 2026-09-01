package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.MainPage;
import pages.OrderPage;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTests extends BaseTest {
    @ParameterizedTest
    @CsvSource({
            "true, Иван, Петров, Москва, Александровский сад, 89991112233, 01.09.2026, двое суток, серый, Привезите к 10:00",
            "false, Елена, Иванова, Москва, Чистые пруды, 89993334455, 03.09.2026, трое суток, чёрный, Позвоните за 10 минут"
    })
    public void orderShouldBeCreatedSuccessfully(
            boolean useTopButton,
            String firstName,
            String lastName,
            String address,
            String metroStation,
            String phone,
            String deliveryDate,
            String rentalPeriod,
            String color,
            String comment) {

        // Открываем главную страницу
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForLoad();

        // Нажимаем "Заказать"
        if (useTopButton) {
            mainPage.clickTopOrderButton();
            System.out.println("Клик по верхней кнопке 'Заказать'");
        } else {
            mainPage.clickBottomOrderButton();
            System.out.println("Клик по нижней кнопке 'Заказать'");
        }

        // Работаем с формой заказа
        OrderPage orderPage = new OrderPage(driver);
        orderPage.waitForLoad();

        // === Первая форма ===
        orderPage.fillName(firstName);
        orderPage.fillLastName(lastName);
        orderPage.fillAddress(address);
        orderPage.fillMetroStation(metroStation);
        orderPage.fillPhone(phone);

        // Переходим ко второй форме
        orderPage.clickNextButton();

        // === Вторая форма ===
        orderPage.fillDeliveryDate(deliveryDate);
        orderPage.selectRentalPeriod(rentalPeriod);
        orderPage.selectColor(color);
        orderPage.fillComment(comment);

        // Нажимаем "Заказать"
        orderPage.clickOrderButton();

        // Подтверждаем заказ
        orderPage.clickConfirmButton();

        // Проверяем результат
        assertTrue(
                orderPage.isSuccessMessageDisplayed(),
                "Сообщение об успешном заказе не отображается"
        );
        String successMessage = orderPage.getSuccessMessage();
        assertTrue(
                successMessage.contains("Заказ оформлен"),
                "Сообщение содержит неправильный текст: "
                        + successMessage
        );
    }
}