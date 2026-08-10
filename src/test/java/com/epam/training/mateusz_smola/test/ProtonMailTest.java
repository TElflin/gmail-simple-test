package com.epam.training.mateusz_smola.test;

import com.epam.training.mateusz_smola.driver.DriverManager;
import com.epam.training.mateusz_smola.page.EmailMainPage;
import com.epam.training.mateusz_smola.page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class ProtonMailTest {

    private static final String USERNAME = "selenium.test.epam";// @proton.me
    private static final String PASSWORD = "Selenium123!";

    @BeforeTest
    void setup() {

        DriverManager.setDriver();
    }


    @Test
    void successfulLogin() {
        EmailMainPage mainPage = logging();
        assertTrue(mainPage.foundNewMailButton(), "Successfully logged and found button");
    }

    @Test
    void SavingDraft() throws InterruptedException {
        EmailMainPage mainPage = logging();
        mainPage.saveDraft();
        Thread.sleep(5000);
    }

//    @Test
//    void openPage() {
//        WebDriver driver = DriverManager.getDriver();
//        LoginPage loginPage = new LoginPage(driver);
//        loginPage.openPage().logIn(USERNAME, PASSWORD);
//
//    }

    @AfterTest
    void teardown() {
        DriverManager.quitDriver();
    }

    private EmailMainPage logging(){
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.openPage().logIn(USERNAME, PASSWORD);
    }
}
