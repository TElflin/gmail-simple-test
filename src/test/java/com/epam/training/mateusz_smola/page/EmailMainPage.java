package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EmailMainPage extends AbstractPage{

    public static final String EMAIL = "you.are@beautiful.pl";
    public static final String MAIL_SUBJECT = "Keep smiling";
    public static final String MAIL_CONTENT = "Keep going \n" +
            "Keep being cool";
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

    @FindBy (css = "a[data-testid=\"navigation-link:all-drafts\"]")
    WebElement draftPageLink;

    @FindBy (css = "div[data-testid*=\"message-item\"]")
    List<WebElement> draftedMessages;

    public EmailMainPage (WebDriver driver){
        super(driver);
    }

    public boolean foundNewMailButton(){
        waitForElement(newMailButton);
        return newMailButton.isDisplayed();
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
        addresseeField.sendKeys(EMAIL);
        waitForElement(subjectField);
        subjectField.sendKeys(MAIL_SUBJECT);
        switchToIframe();
        waitForElement(messageField);
        messageField.clear();
        messageField.sendKeys(MAIL_CONTENT);
        return this;
    }

    public EmailMainPage saveDraft(){
        createNewDraft();
        closeMessage();
        return this;
    }


    public boolean checkForDraft() {
        waitForElement(draftPageLink);
        draftPageLink.click();
        waitForMessageList();
        int listSize = draftedMessages.size();

        for (int i = 0; i < listSize; i++){

            List<WebElement> freshList =  driver.findElements(
                    By.cssSelector("div[data-testid*=\"message-item\"]"));
            freshList.get(i).click();

            if(checkForSearchedMessage()) {
                switchToDefaultContent();
                return true;
            }

            switchToDefaultContent();
            draftPageLink.click();
            waitForMessageList();
        }

        return false;
    }

    private void waitForMessageList() {
        waitForElement(draftedMessages.getFirst());
    }


    private boolean checkForSearchedMessage(){
        boolean isSearchedMessage = true;

        waitForElement(addresseeField);
        if (!addresseeField.getText().equals(EMAIL)) { isSearchedMessage = false; };
        waitForElement(subjectField);
        if (!subjectField.getText().equals(MAIL_SUBJECT)) { isSearchedMessage = false; }
        switchToIframe();
            waitForElement(messageField);
            if (!messageField.getText().equals(MAIL_CONTENT)) { isSearchedMessage = false; }
        switchToDefaultContent();
        return isSearchedMessage;
    }

    private void closeMessage() {
        messageField.sendKeys(Keys.ESCAPE);
        driver.switchTo().defaultContent();
    }

    private void switchToDefaultContent(){
        driver.switchTo().defaultContent();
    }


    private void switchToIframe() {
        waitForElement(messageIframe);
        driver.switchTo().frame(messageIframe);
    }
}
