import org.junit.jupiter.api.BeforeEach;
import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;


import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CardDeliveryOrderTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999/");
    }
    String dateGenerator(int dayToAdd) {
        return java.time.LocalDate.now()
                .plusDays(dayToAdd).format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void SuccessfulApplicationSubmission () {
        $("[data-test-id='city'] input").setValue("Уфа");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(5));
        $("[data-test-id='name'] input").setValue("Петрова Юля");
        $("[data-test-id='phone'] input").setValue("+78994432567");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='notification']").should(visible, Duration.ofMillis(15000))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + dateGenerator(5)));
    }

    @Test
    void WrongCityEntry () {
            $("[data-test-id='city'] input").setValue("Камышлы");
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(dateGenerator(5));
            $("[data-test-id='name'] input").setValue("Петрова Юля");
            $("[data-test-id='phone'] input").setValue("+78994432567");
            $("[data-test-id='agreement'] .checkbox__text").click();
            $("button.button_theme_alfa-on-white").click();
            $("[data-test-id='city'].input_invalid .input__sub")
                    .shouldBe(visible).shouldHave(text("Доставка в выбранный город недоступна"));
        }

        @Test
    void BookingForPastDate () {
                $("[data-test-id='city'] input").setValue("Уфа");
                $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
                $("[data-test-id='date'] input").setValue(dateGenerator(1));
                $("[data-test-id='name'] input").setValue("Петрова Юля");
                $("[data-test-id='phone'] input").setValue("+78994432567");
                $("[data-test-id='agreement'] .checkbox__text").click();
                $("button.button_theme_alfa-on-white").click();
                $("[data-test-id='date'] .input_invalid .input__sub").shouldBe(visible)
                        .shouldHave(text("Заказ на выбранную дату невозможен"));
        }

        @Test
    void NameEntryInLatin () {
            $("[data-test-id='city'] input").setValue("Уфа");
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(dateGenerator(5));
            $("[data-test-id='name'] input").setValue("Petrova Julia");
            $("[data-test-id='phone'] input").setValue("+78994432567");
            $("[data-test-id='agreement'] .checkbox__text").click();
            $("button.button_theme_alfa-on-white").click();
            $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                    .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
    void PhoneNumberEnteredIncorrectly () {
            $("[data-test-id='city'] input").setValue("Уфа");
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(dateGenerator(5));
            $("[data-test-id='name'] input").setValue("Петрова Юля");
            $("[data-test-id='phone'] input").setValue("+74432567");
            $("[data-test-id='agreement'] .checkbox__text").click();
            $("button.button_theme_alfa-on-white").click();
            $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible)
                    .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
    void SendingARequestWithoutAgreement () {
            $("[data-test-id='city'] input").setValue("Уфа");
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(dateGenerator(5));
            $("[data-test-id='name'] input").setValue("Петрова Юля");
            $("[data-test-id='phone'] input").setValue("+78994432567");
            $("[data-test-id='agreement'] .checkbox__text").click();
            $("button.button_theme_alfa-on-white").click();
            $("button.button_theme_alfa-on-white").click();
            assertTrue($("[data-test-id='agreement'].input_invalid .checkbox__text").isDisplayed());
        }
    }


