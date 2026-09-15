package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.epam.training.mateusz_smola.model.User;

public class LoginPage extends AbstractPage {


    @FindBy (css = "#username")
    private WebElement usernameField;

    @FindBy(css = "#password")
    private WebElement passwordField;

    @FindBy (css = "button[type=\"submit\"]")
    private WebElement submitButton;

    public LoginPage (WebDriver driver) {
        super(driver);
    }

    public LoginPage openPage (Email email){
        openPage(email.getPageUrl());
        return this;
    }

    public EmailMainPage logIn(User user){
        enterCredentials(user.getUsername(),user.getPassword());
        submitButton.click();
        return new EmailMainPage(driver);
    }

    public LoginPage enterLogin (String username){
        waitForElement(usernameField);
        usernameField.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword ( String password){
        waitForElement(passwordField);
        passwordField.sendKeys(password);
        return this;
    }

    public void enterCredentials (String username, String password){
        enterLogin(username);
        enterPassword(password);
    }


}
