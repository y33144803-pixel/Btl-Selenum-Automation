import org.example.pages.HomePage; // ייבוא של דף הבית שיצרת
import org.junit.jupiter.api.*; // אנוטציות של JUnit 5
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BtlSearchTest {

    private WebDriver driver;
    private HomePage homePage;
    private final String SEARCH_TERM = "חישוב סכום דמי לידה ליום";
//    private final String SEARCH_TERM = "חישוב סכום דמי אבטלה";

    @BeforeEach // מחליף את BeforeClass/BeforeMethod ב-JUnit 5
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // הגדרת המתנה מובנית (Implicit Wait) כגיבוי
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // ניווט לאתר ביטוח לאומי
        driver.get("https://www.btl.gov.il/Pages/default.aspx");

        // אתחול דף הבית
        homePage = new HomePage(driver);
    }

    @Test
    @DisplayName("בדיקת פונקציונליות חיפוש באתר ביטוח לאומי")
    public void testSearchFunctionality() {
        // 1. ביצוע החיפוש
        homePage.performSearch(SEARCH_TERM);

        // 2. בדיקת התוצאה הצפויה
        String actualTitle = driver.getTitle();

        // ב-JUnit 5 משתמשים ב-Assertions.assertTrue
        Assertions.assertTrue(actualTitle.contains("תוצאות חיפוש") || driver.getPageSource().contains(SEARCH_TERM),
                "החיפוש נכשל או שהדף לא נטען כראוי");

        System.out.println("החיפוש בוצע בהצלחה עבור: " + SEARCH_TERM);
    }

    @AfterEach // מחליף את AfterClass/AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}