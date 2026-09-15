package com.epam.training.mateusz_smola.test;

import com.epam.training.mateusz_smola.driver.DriverManager;
import com.epam.training.mateusz_smola.model.Email;
import com.epam.training.mateusz_smola.page.EmailMainPage;
import com.epam.training.mateusz_smola.page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static com.epam.training.mateusz_smola.service.EmailCreator.createEmail;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class ProtonMailTest {


    @BeforeMethod
    void setup() {
        DriverManager.setDriver();

    }


    @Test
    void successfulLogin() {
        EmailMainPage mainPage = logging();
        assertTrue(mainPage.foundNewMailButton(), "Successfully logged and found button");
    }

    @Test//(dependsOnMethods = "successfulLogin")
    void creatingAndSavingDraft() {
        EmailMainPage mainPage = logging();
        assertTrue(mainPage.saveDraft().checkForDraft(),"Draft wasn't saved");

    }

    @Test//(dependsOnMethods = "creatingAndSavingDraft")
    void sendingMailDeletingFromDrafts() {
        EmailMainPage mainPage = logging();

        mainPage.openDraft().sendMail();
        assertFalse(mainPage.checkForDraft(), "Draft didn't disappear after sending");
    }

    @Test//(dependsOnMethods = "sendingMailDeletingFromDrafts")
    void mailIsInSendFolder(){
        EmailMainPage mainPage = logging();

        assertTrue(mainPage.checkForSent(), "No email in send folder");
    }
    @AfterMethod
    void teardown() {
        DriverManager.quitDriver();
    }

    private EmailMainPage logging(){
        Email email = createEmail();
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.openPage(email).logIn(email.getUser());
    }
}
