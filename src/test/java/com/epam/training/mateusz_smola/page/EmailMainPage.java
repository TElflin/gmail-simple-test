package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EmailMainPage extends AbstractPage{

    public static final String BY_FOR_EMAIL_LIST = "div[class=\"item-container-wrapper relative\"]";
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

    @FindBy (css = BY_FOR_EMAIL_LIST)
    List<WebElement> draftedMessages;

    @FindBy ( css = "[data-testid=\"composer-addresses-item-label\"]")
    WebElement messageAddress;



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

            draftedMessages.get(i).click();
            if (!isIFrameOpen(messageIframe)){
                draftedMessages.get(i).click();
            }
            System.out.println("Kurwa!!!!");
            if(checkForSearchedMessage()) {

                return true;
            }

            switchToDefaultContent();
            draftPageLink.click();
            waitForMessageList();
        }

        return false;
    }

    private void waitForMessageList() {
        //TODO If there is no messages it work to fast and ther is null pointer
        waitForElement(draftedMessages.getFirst());
    }

    private boolean isIFrameOpen(WebElement IFrame){
        try {
            waitForElement(IFrame);
            return true;
        }
        catch (TimeoutException e){
            return false;
        }
    }

    private boolean checkForSearchedMessage(){
        boolean isSearchedMessage = true;
        System.out.println("Kurwa0");
        waitForElement(messageAddress);
        System.out.println("Kurwa1");
        if (!messageAddress.getText().contains(EMAIL)) { isSearchedMessage = false; }
        waitForElement(subjectField);
        System.out.println("Kurwa2");
        if (!subjectField.getAttribute("value").equals(MAIL_SUBJECT)) { isSearchedMessage = false; }
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
