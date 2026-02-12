package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // פונקציית ה-initElements מאתחלת את כל ה-@FindBy במחלקה
        PageFactory.initElements(driver, this);
    }
}
