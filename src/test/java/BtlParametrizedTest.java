import org.example.pages.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BtlParametrizedTest {
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

    @ParameterizedTest
    @DisplayName("בדיקת Breadcrumbs ל-5 דפים שונים")
    @CsvSource({
            "אבטלה, אבטלה",
            "זיקנה, קצבת זיקנה",
            "ילדים, קצבת ילדים",
            "נכות כללית, נכות כללית",
            "סיעוד, גמלת סיעוד"
    })
    public void testBreadcrumbsNavigation(String subMenu, String expectedBreadcrumb) {
        // ניווט לדף דרך תפריט קצבאות והטבות
        homePage.navigateTo(MainMenu.BENEFITS, subMenu);

        // קבלת הטקסט מה-Breadcrumbs
        String actualBreadcrumb = homePage.getBreadcrumbsText();

        // בדיקה שהדף הנכון עלה
        Assertions.assertTrue(actualBreadcrumb.contains(expectedBreadcrumb),
                "ה-Breadcrumb לא הכיל את הטקסט הצפוי: " + expectedBreadcrumb);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}