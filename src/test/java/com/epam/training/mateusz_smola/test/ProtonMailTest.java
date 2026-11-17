package com.epam.training.mateusz_smola.test;

import com.epam.training.mateusz_smola.driver.DriverManager;
import com.epam.training.mateusz_smola.model.Email;
import com.epam.training.mateusz_smola.model.User;
import com.epam.training.mateusz_smola.page.EmailMainPage;
import com.epam.training.mateusz_smola.page.LoginPage;
import com.epam.training.mateusz_smola.util.TestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.epam.training.mateusz_smola.service.EmailCreator.createEmail;
import static com.epam.training.mateusz_smola.service.UserCreator.CreateUser;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Listeners(TestListener.class)
public class ProtonMailTest {


    @BeforeMethod
    void setup() {
        DriverManager.getInstance().setDriver();

    }


    @Test
    void successfulLogin() {
        EmailMainPage mainPage = logging();
        assertTrue(mainPage.foundNewMailButton(), "Successfully logged and found button");
    }

    @Test
    void creatingAndSavingDraft() {
        EmailMainPage mainPage = logging();
        Email email = createEmail();
        assertTrue(mainPage.saveDraft(email).checkForDraft(email), "Draft wasn't saved");
    }

    @Test
    void sendingMailDeletingFromDrafts() {
        EmailMainPage mainPage = logging();
        Email email = createEmail();
        mainPage.sendDraft(email);
        assertFalse(mainPage.checkForDraft(email), "Draft didn't disappear after sending");
    }

    @Test
    void mailIsInSendFolder() {
        EmailMainPage mainPage = logging();
        Email email = createEmail();
        assertTrue(mainPage.checkForSent(email), "No email in send folder");
    }

    @AfterMethod
    void teardown() {
        DriverManager.getInstance().quitDriver();
    }

    private EmailMainPage logging() {
        User user = CreateUser();
        WebDriver driver = DriverManager.getInstance().getDriver();
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.openPage().logIn(user);
    }

}
