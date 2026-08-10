package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    private static final String PAGE_URL = "https://account.proton.me/pl/mail";

    @FindBy (css = "#username")
    private WebElement usernameField;

    @FindBy(css = "#password")
    private WebElement passwordField;

    @FindBy (css = "button[type=\"submit\"]")
    private WebElement submitButton;

    public LoginPage (WebDriver driver) {
        super(driver);
    }

    public LoginPage openPage (){
        openPage(PAGE_URL);
        return this;
    }

    public EmailMainPage logIn(String username, String password){
        enterCredentials(username,password);
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
