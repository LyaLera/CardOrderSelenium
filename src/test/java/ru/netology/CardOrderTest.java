package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendFormIfNameWithHyphenAndSpace() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванова-Левина Ольга");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSendFormIfOneLetterInName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("И");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSendFormIfMoreThanSeventyLettersInName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иоадыжфшутснвлфьыосладм ровлаыдвлаьморсиивлыоооооо ооооооооодддддддддды");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual);
    }


    @Test
    public void shouldNotSendFormIfLatinLettersInName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanova Anna");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Фамилия и имя\nИмя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfSymbolsInName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("&*$%");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Фамилия и имя\nИмя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfNumbersInName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("8598609476");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Фамилия и имя\nИмя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfNameEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79384615869");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Фамилия и имя\nПоле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfPhoneEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Мобильный телефон\nПоле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfPhoneWithUnderLimitNumber() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("7");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Мобильный телефон\nТелефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfPhoneWithOverLimitNumber() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+790293857403");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Мобильный телефон\nТелефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfPhoneWithLetters() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("Ldsfaрппоо");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Мобильный телефон\nТелефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormIfPhoneWithSymbols() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("&%*&(");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Мобильный телефон\nТелефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendFormWithoutAgreement() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Игорь Петрович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+74837495847");
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expected, actual);
    }


}
