import org.example.pages.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BtlToSonTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.btl.gov.il/Pages/default.aspx");
        homePage = new HomePage(driver);
    }

    @Test
    @DisplayName("סעיף 6: חישוב דמי ביטוח לאומי לתלמיד ישיבה - סכום כולל מתוך DOM")
    public void testYeshivaStudentCalculator() {
        // 1. ניווט לקטגוריית ביטוח לאומי
        homePage.goToBtlInsurance();

        // 2. לחיצה על קישור המחשבון
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'מחשבון לחישוב דמי ביטוח') or contains(text(),'מחשבון')]")
        ));
        calculatorLink.click();

        // 3. וידוא שהעגנו במחשבון
        Assertions.assertTrue(driver.getTitle().contains("חישוב דמי ביטוח"), "לא הגענו לדף המחשבון הנכון");

        // 4. מילוי שלב א'
        String birthDate = LocalDate.now().minusYears(20).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BtlToSon calcPage = new BtlToSon(driver);
        calcPage.fillFirstStep(birthDate);

        // 5. שלב ב' - אשר שנפתח ונבחר "לא"
        Assertions.assertTrue(driver.getPageSource().contains("מקבל קצבת נכות"), "לא עבר לשלב השני");
        calcPage.fillSecondStep();

        // 6. המתן להופעת תוצאת החישוב
        WebElement calcResultUl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.CalcResult"))
        );

        // מחפש את <li> שמכיל "סך הכל דמי ביטוח לחודש"
        WebElement totalLi = calcResultUl.findElement(
                By.xpath(".//li[contains(text(),'סך הכל דמי ביטוח לחודש')]")
        );

        // שולף את המספר מה-<strong>
        String actualSum = totalLi.findElement(By.tagName("strong")).getText()
                .replace(",", "")        // הסר פסיקים
                .replace(".00", "")      // הסר .00 (אם יש)
                .trim();

        Assertions.assertEquals("171", actualSum, "סכום החיוב הכולל אינו תואם לסכום הצפוי (171).");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
             driver.quit();
        }
    }
}