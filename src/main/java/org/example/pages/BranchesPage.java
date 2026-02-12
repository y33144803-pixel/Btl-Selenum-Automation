package org.example.pages;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BranchesPage extends BtlBasePage {
    public BranchesPage(WebDriver driver) {
        super(driver);
    }

    public static class MainTest {
        public static void main(String[] args) {
            // 1. הגדרת הדרייבר (באמצעות התלות של WebDriverManager מה-POM)
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();

            try {
                // 2. הגדרות בסיסיות של הדפדפן
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

                // 3. ניווט לאתר ביטוח לאומי
                driver.get("https://www.btl.gov.il/Pages/default.aspx");

                // 4. יצירת מופע של דף הבית (כאן מתבצע ה-initElements)
                HomePage homePage = new HomePage(driver);

                // 5. הרצת המשימות שבנית:


                // ג. מעבר לדף סניפים (הפונקציה מחזירה דף חדש)
                homePage.goToBranches();

                System.out.println("הבדיקה עברה בהצלחה!");

            } catch (Exception e) {
                System.out.println("קרתה שגיאה במהלך ההרצה: " + e.getMessage());
            } finally {
                 driver.quit();
            }
        }
    }
}