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
    @FindBy ( css = "button[data-testid=\"sidebar:compose\"][type=\"button\"]")
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

    @FindBy ( css = "span.composer-addresses-fakefield-inner")
    WebElement messageAddress;

    @FindBy ( css ="[data-testid=\"composer:close-button\"]")
    WebElement messageCloseButton;


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
            if(checkForSearchedMessage()) {
                return true;
            }
            switchToDefaultContent();
            draftPageLink.click();
            waitForMessageList();
        }
        return false;
    }

    private boolean checkForSearchedMessage(){
        boolean isSearchedMessage = true;
        waitForElement(messageAddress);
        if (!messageAddress.getAttribute("title").contains(EMAIL)) { isSearchedMessage = false; }
        waitForElement(subjectField);
        if (!subjectField.getAttribute("value").equals(MAIL_SUBJECT)) { isSearchedMessage = false; }
        switchToIframe();
            waitForElement(messageField);
            if (!messageField.getText().equals(MAIL_CONTENT)) { isSearchedMessage = false; }
        switchToDefaultContent();
        return isSearchedMessage;
    }

    private void closeMessage() {
        driver.switchTo().defaultContent();
        WebElement closeButton = waitForClickable(messageCloseButton);
        closeButton.click();
        waitForClosing();
    }

    private void switchToDefaultContent(){
        driver.switchTo().defaultContent();
    }

    private void switchToIframe() {
        waitForElement(messageIframe);
        driver.switchTo().frame(messageIframe);
    }

    private void waitForMessageList() {
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(BY_FOR_EMAIL_LIST)
                ));
    }

    private void waitForClosing() {
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.invisibilityOf(messageIframe));
    }
}
