package com.epam.training.mateusz_smola.test;

import com.epam.training.mateusz_smola.driver.DriverManager;
import com.epam.training.mateusz_smola.page.EmailMainPage;
import com.epam.training.mateusz_smola.page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static org.testng.Assert.assertTrue;


public class ProtonMailTest {

    private static final String USERNAME = "selenium.test.epam";// @proton.me
    private static final String PASSWORD = "Selenium123!";

    @BeforeMethod
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
        assertTrue(mainPage.saveDraft().checkForDraft(),"Draft was saved");
        Thread.sleep(5000);
    }

    @Test
    void openPage() {
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openPage().logIn(USERNAME, PASSWORD);

    }

    @AfterMethod
    void teardown() {
        DriverManager.quitDriver();
    }

    private EmailMainPage logging(){
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.openPage().logIn(USERNAME, PASSWORD);
    }
}
