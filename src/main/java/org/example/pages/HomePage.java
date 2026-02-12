//package org.example.pages;
//
//// HomePage.java
//
//import org.openqa.selenium.WebDriver;
//
//public class HomePage extends BtlBasePage {
//    public HomePage(WebDriver driver) {
//        super(driver);
//    }
//}
//
//
//
//


package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage extends BtlBasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * ניווט ישיר מקישור "דמי ביטוח לאומי"
     */
    public void goToBtlInsurance() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("דמי ביטוח לאומי")));
        link.click();
    }
}