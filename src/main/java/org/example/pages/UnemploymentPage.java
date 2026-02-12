package org.example.pages;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BtlParametrizedTest {
    private WebDriver driver;
    private WebDriverWait wait;

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.btl.gov.il/Pages/default.aspx");
    }

    @ParameterizedTest
    @DisplayName("בדיקת breadcrumbs ל-5 דפים שונים בתפריט קצבאות והטבות")
    @CsvSource({
            "אבטלה, אבטלה",
            "זיקנה, קצבת זיקנה",
            "ילדים, קצבת ילדים",
            "נכות כללית, נכות כללית",
            "סיעוד, גמלת סיעוד"
    })
    public void testBreadcrumbsNavigation(String submenu, String expectedBreadcrumb) {
        // 1. פתח תפריט "קצבאות והטבות"
        WebElement benefitsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'קצבאות והטבות')]")));
        scrollToElement(benefitsMenu);
        benefitsMenu.click();

        // 2. לחץ תת-תפריט
        WebElement subMenuElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'" + submenu + "')]")));
        scrollToElement(subMenuElement);
        subMenuElement.click();

        // 3. המתן לזיהוי ה־breadcrumbs
        WebElement breadcrumbs = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".breadcrumb, .breadcrumbs, nav[aria-label='breadcrumb'] span, ol.breadcrumb")));

        // 4. שלוף טקסט ואמת
        String breadcrumbsText = breadcrumbs.getText();
        Assertions.assertTrue(breadcrumbsText.contains(expectedBreadcrumb),
                "ה-breadcrumb לא הכיל את הטקסט הצפוי: " + expectedBreadcrumb);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}}