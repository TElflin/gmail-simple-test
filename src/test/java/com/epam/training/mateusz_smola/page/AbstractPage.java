package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AbstractPage {

    protected WebDriver driver;

    protected AbstractPage(WebDriver driver){
        PageFactory.initElements(driver,this);
        this.driver = driver;
    }
    protected void openPage(String url){
        driver.get(url);
    }

    protected void waitForElement(WebElement element){
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOf(element));
    }


}
