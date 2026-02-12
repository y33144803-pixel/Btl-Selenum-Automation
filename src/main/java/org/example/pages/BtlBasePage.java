package org.example.pages;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BtlBasePage extends BasePage {
    @FindBy(id = "TopQuestions")
    private WebElement searchField;

    @FindBy(id = "ctl00_SiteHeader_reserve_btnSearch")
    private WebElement searchIcon;

    @FindBy(linkText = "סניפים")
    private WebElement branchesButton;
    @FindBy(linkText = "אשדוד")
    private WebElement branchesCityButton;
    @FindBy(className = "bread_crumbs")
    private WebElement breadcrumbs;

    public String getBreadcrumbsText() {
        return breadcrumbs.getText();
    }
// הוסיפי את הפונקציה הזו לתוך BtlBasePage.java

    public void navigateTo(MainMenu menu, String subMenuName) {
        clickMainMenu(menu);
        clickSubMenu(subMenuName);
    }
    public BtlBasePage(WebDriver driver) {
        super(driver);
    }
    public void clickMainMenu(MainMenu menu) {
        // XPath גמיש יותר שמחפש טקסט בתוך הניווט הראשי
        String xpath = "//ul[@id='main_nav']//*[contains(text(),'" + menu.getText() + "')]";

        // המתנה מפורשת קצרה כדי לוודא שהאלמנט מוכן ללחיצה
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

        menuElement.click();
    }

    public void clickSubMenu(String subMenuName) {
        String xpath = "//ul[@class='sub_menu']//a[contains(text(),'" + subMenuName + "')]";
        driver.findElement(By.xpath(xpath)).click();
    }

    public void performSearch(String text) {
        // יצירת אובייקט המתנה של עד 10 שניות
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // המתנה עד שהאלמנט יהיה קיים בדף וגם מוצג לעין
            wait.until(ExpectedConditions.visibilityOf(searchField));

            searchField.clear();
            searchField.sendKeys(text);
            searchIcon.click();
        } catch (Exception e) {
            System.out.println("לא הצלחתי למצוא את שדה החיפוש תוך 10 שניות.");
            throw e; // זריקת השגיאה כדי שהטסט ייכשל בצורה ברורה
        }
    }

    public BranchesPage goToBranches() {

        branchesButton.click();
        branchesCityButton.click();


        // בדיקה אם האלמנט קיים
        if (driver.getPageSource().contains("כתובת")&&
            driver.getPageSource().contains("מענה טלפוני")&&
               driver.getPageSource().contains("קבלת קהל"))
                    System.out.println("הטקסט  נמצא בעמוד.");
       else
        System.out.println("הטקסט  לא נמצא בעמוד.");
       return new BranchesPage(driver);
    }
}
