package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EmailMainPage extends AbstractPage{

    @FindBy ( css = "button[class=\"button button-large button-solid-norm w-full hidden md:inline\"]")
    WebElement newMailButton;

    @FindBy ( css = "[id^=\"to-composer\"]")
    WebElement addresseeField;

    @FindBy (css = "[id^=\"subject-composer\"]")
    WebElement subjectField;

    @FindBy (css = "iframe[data-testid=\"rooster-iframe\"]")
    WebElement messageIframe;

    @FindBy (id = "rooster-editor")
    WebElement messageField;

    @FindBy (css = "data-testid=\"navigation-link:all-drafts\"")
    WebElement draftPageLink;

    @FindBy (id = "div[data-testid=\"message-item:Keep smiling\"")
    WebElement draftedMessage;

    public EmailMainPage (WebDriver driver){
        super(driver);
    }

    public boolean foundNewMailButton(){
        waitForElement(newMailButton);
        return newMailButton.isDisplayed();
    }

    public EmailMainPage saveDraft(){
        createNewDraft();
        messageField.sendKeys(Keys.ESCAPE);
        return this;
    }

    public EmailMainPage createNewDraft()
    {
        waitForElement(newMailButton);
        newMailButton.click();
        fillNewMailFields();
        return this;
    }

    public EmailMainPage fillNewMailFields(){
        waitForElement(addresseeField);
        addresseeField.sendKeys("you.are@beautiful.pl");
        waitForElement(subjectField);
        subjectField.sendKeys("Keep smiling");
        switchToIframe();
        waitForElement(messageField);
        messageField.clear();
        messageField.sendKeys("Keep going \n" +
                "Keep being cool");
        return this;
    }


    private void switchToIframe() {
        waitForElement(messageIframe);
        driver.switchTo().frame(messageIframe);
    }

    private void switchToDefaultContent(){
        driver.switchTo().defaultContent();
    }

}
