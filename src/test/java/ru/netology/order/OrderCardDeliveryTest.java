package ru.netology.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.generator.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class OrderCardDeliveryTest {
    DataGenerator dataGenerator = new DataGenerator();
    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        String name = dataGenerator.getName();
        String phone = dataGenerator.getPhone();
        String city = dataGenerator.getCity();

        $("[placeholder='Город']").setValue(city);
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(name);
        $("[name=phone]").setValue(phone);
        $(".checkbox__box").click();
        $(".button__text").click();
        $(withText("Успешно")).shouldBe(visible);
        $("input[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(4));
        $(".button__text").click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id=replan-notification] button.button").click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shouldNotSubmitWithoutCity() {
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.getName());
        $("[name=phone]").setValue(dataGenerator.getPhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitWithoutName() {
        $("[placeholder='Город']").setValue(dataGenerator.getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=phone]").setValue(dataGenerator.getPhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitWithIncorrectName() {
        $("[placeholder='Город']").setValue(dataGenerator.getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue("Vasiliy Ivanov");
        $("[name=phone]").setValue(dataGenerator.getPhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSubmitWithoutPhone() {
        $("[placeholder='Город']").setValue(dataGenerator.getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.getName());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitWithoutCheckbox() {
        $("[placeholder='Город']").setValue(dataGenerator.getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.getName());
        $("[name=phone]").setValue(dataGenerator.getPhone());
        $(".button__text").click();
        $(".input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}