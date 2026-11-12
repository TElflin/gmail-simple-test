package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.epam.training.mateusz_smola.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static final String LOGIN_PAGE_URL = "https://account.proton.me/pl/mail";

    @FindBy(css = "#username")
    private WebElement usernameField;

    @FindBy(css = "#password")
    private WebElement passwordField;

    @FindBy(css = "button[type=\"submit\"]")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage openPage() {
        logger.info("ACTION: Opening login page for email flow (url: {})", LOGIN_PAGE_URL);
        openPage(LOGIN_PAGE_URL);
        return this;
    }

    public EmailMainPage logIn(User user) {
        logger.info("ACTION: Logging in as user: {}", user.getUsername());
        enterCredentials(user.getUsername(), user.getPassword());
        submitButton.click();
        logger.debug("Submit button clicked, navigating to EmailMainPage");
        return new EmailMainPage(driver);
    }

    public LoginPage enterLogin(String username) {
        logger.debug("Entering username: {}", username);
        waitForElement(usernameField);
        usernameField.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        logger.debug("Entering password: ****");
        waitForElement(passwordField);
        passwordField.sendKeys(password);
        return this;
    }

    public void enterCredentials(String username, String password) {
        logger.debug("Entering credentials for user: {}", username);
        enterLogin(username);
        enterPassword(password);
    }

}