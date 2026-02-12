package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InsuranceCalculatorPage extends BtlBasePage {

    @FindBy(id = "BirthDate") // יש לוודא ID מדויק באתר
    private WebElement birthDateField;

    @FindBy(id = "btnNext") // כפתור המשך
    private WebElement nextButton;

    public InsuranceCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void enterBirthDate(String date) {
        birthDateField.sendKeys(date);
    }

    public void clickNext() {
        nextButton.click();
    }

    // כאן יוסיפו פונקציות לבדיקת התוצאות (סעיף 6)
}