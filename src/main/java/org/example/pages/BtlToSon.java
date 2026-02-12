package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BtlToSon extends BtlBasePage {

    @FindBy(linkText = "מחשבון לחישוב דמי ביטוח")
    private WebElement calcLink;

    @FindBy(css = "input[name$='$Date']")
    private WebElement birthDateField;

    @FindBy(xpath = "//label[contains(text(),'תלמיד ישיבה')]")
    private WebElement yeshivaStudentOption;

    @FindBy(xpath = "//label[contains(.,'זכר')]")
    private WebElement genderMaleRadio;

    @FindBy(xpath = "//label[contains(.,'נקבה')]")
    private WebElement genderFemaleRadio;

    @FindBy(css = "input[type='submit'][value='המשך']")
    private WebElement nextButton;

    // שימוש ב-label לכפתור "לא", עמיד גם אם לא משנה מיקום ה-input
    @FindBy(xpath = "//label[normalize-space(text())='לא']")
    private WebElement labelNoOption;

    public BtlToSon(WebDriver driver) {
        super(driver);
    }

    public void goToCalculator() {
        calcLink.click();
    }

    /**
     * מילוי שלב ראשון: בחירת תלמיד ישיבה, מין, תאריך לידה, המתנה לטעינת שלב 2
     * @param birthDate תאריך לידה בפורמט dd/MM/yyyy
     */
    public void fillFirstStep(String birthDate) {
        yeshivaStudentOption.click();
        genderMaleRadio.click(); // או genderFemaleRadio
        birthDateField.clear();
        birthDateField.sendKeys(birthDate);
        nextButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'קצבת נכות')]")));
    }

    /**
     * מילוי שלב שני: בחירת "לא" לקצבת נכות והמשך
     */
    public void fillSecondStep() {
        // המתן שיהיה לחיץ (יכול להחליף שנדרש)
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(labelNoOption));
        labelNoOption.click();
        nextButton.click();
    }
}