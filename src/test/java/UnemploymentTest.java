import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UnemploymentTest {
    private WebDriver driver;

    private void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private void triggerInputChange(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));", element);
    }

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.btl.gov.il/Pages/default.aspx");
    }

    @Test
    @DisplayName("סעיף 7: חישוב דמי אבטלה")
    public void testUnemploymentCalculator() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. פתח תפריט "קצבאות והטבות"
        WebElement benefitsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'קצבאות והטבות')]")));
        benefitsMenu.click();

        // 2. לחץ "אבטלה"
        WebElement unemploymentMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'אבטלה')]")));
        unemploymentMenu.click();

        // 3. מחשבוני דמי אבטלה
        WebElement calculatorsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'מחשבוני דמי אבטלה') or contains(text(),'מחשבון')]")
        ));
        calculatorsLink.click();

        // 4. חישוב דמי אבטלה
        WebElement amountCalcLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("חישוב דמי אבטלה")));
        amountCalcLink.click();

        // 5+6. מלא שני שדות תאריך
        String lastMonthDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String registrationDate = LocalDate.now().minusDays(10).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<WebElement> allDateInputs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//input[@type='text' or @type='date']")));

        if (allDateInputs.size() < 2) {
            throw new IllegalStateException("לא נמצאו שני שדות תאריך בדף");
        }

        scrollToElement(driver, allDateInputs.get(0));
        allDateInputs.get(0).clear();
        allDateInputs.get(0).sendKeys(lastMonthDate);

        scrollToElement(driver, allDateInputs.get(1));
        allDateInputs.get(1).click();
        allDateInputs.get(1).clear();
        allDateInputs.get(1).sendKeys(lastMonthDate);
        allDateInputs.get(1).sendKeys(Keys.ENTER);

        // 7. בחירת גיל (28 עד 67) - רק אם האלמנט קיים
        List<WebElement> ageLabels = driver.findElements(By.xpath("//label[contains(text(),'28 עד 67')]"));
        if (!ageLabels.isEmpty()) {
            scrollToElement(driver, ageLabels.get(0));
            ageLabels.get(0).click();
            List<WebElement> nextBtns = driver.findElements(By.id("btnNext"));
            if (!nextBtns.isEmpty()) {
                scrollToElement(driver, nextBtns.get(0));
                nextBtns.get(0).click();
            }
        }

        // --- חכה לטבלת שכר ---
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'סכום הכנסות לחודשים האחרונים')]")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//table"))
        ));

        // 8. מלא שדות שכר בתוך הטבלה בלבד
        List<WebElement> salaryInputs = driver.findElements(
                By.xpath("//table//input[@type='text' or @type='number']")
        );
        for (int i=0; i<salaryInputs.size(); i++) {
            WebElement salaryInput = salaryInputs.get(i);
            salaryInput.clear();
            salaryInput.sendKeys("10000");
            triggerInputChange(driver, salaryInput);
        }

        // איבוד פוקוס (TAB ו-blur) לשדה האחרון בטבלה
        WebElement lastInput = salaryInputs.get(salaryInputs.size() - 1);
        lastInput.sendKeys(Keys.TAB);
        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", lastInput);

        // 9. לחץ על כפתור המשך (id ייחודי!)
        By continueBtnBy = By.id("ctl00_ctl43_g_2ccdbe03_122a_4c30_928f_60300c0df306_ctl00_AvtalaWizard_StepNavigationTemplateContainerID_StepNextButton");
        WebElement nextAfterSalary = wait.until(ExpectedConditions.elementToBeClickable(continueBtnBy));
        scrollToElement(driver, nextAfterSalary);
        nextAfterSalary.click();

        // 10. בדיקת תוצאות החישוב (כפי שנדרש)
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(
                pageSource.contains("שכר יומי ממוצע") &&
                        pageSource.contains("דמי אבטלה ליום") &&
                        pageSource.contains("דמי אבטלה לחודש"),
                "לא נמצאו כל שדות התוצאה בדף החישוב"
        );
    }

    @AfterEach
    public void tearDown() {
//        if (driver != null) driver.quit();
    }
}