package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EmailMainPage extends AbstractPage{

    public static final String BY_FOR_EMAIL_LIST = "div[class=\"item-container-wrapper relative\"]";
    public static final String BY_FOR_SEND_BUTTON = "[data-testid=\"composer:send-button\"]";
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

    @FindBy (css = "a[data-testid=\"navigation-link:all-sent\"]")
    WebElement sendPageLink;

    @FindBy (css = BY_FOR_EMAIL_LIST)
    List<WebElement> draftedMessages;

    @FindBy ( css = "span.composer-addresses-fakefield-inner")
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
        int index = findDraftIndex();
        if (index != -1) {
            closeComposer();
            return true;
        }
        return false;
    }
    public boolean checkForSent() {
        int index = findSentIndex();
        if (index != -1) {

            return true;
        }
        return false;
    }

    public EmailMainPage openDraft() {
        int index = findDraftIndex();
        if (index == -1) {
            throw new NoSuchElementException("Matching draft not found");
        }
        return this;
    }

    public EmailMainPage sendMail(){
        clickSendButton();
        waitForSendConfirmationToClear();
        switchToDefaultContent();
        return this;
    }

    private int findDraftIndex() {
        waitForElement(draftPageLink);
        draftPageLink.click();
        waitForMessageList();

        int listSize = draftedMessages.size();

        for (int i = 0; i < listSize; i++) {
            clickDraftByIndex(i);
            if (checkForSearchedMessage()) {
                return i;
            }
            switchToDefaultContent();
            closeComposer();

            draftPageLink.click();
            waitForMessageList();
        }
        return -1;
    }

    private int findSentIndex() {
        waitForElement(sendPageLink);
        sendPageLink.click();
        waitForMessageList();

        int listSize = draftedMessages.size();

        for (int i = 0; i < listSize; i++) {
            if (checkSentHeader(i)) {
                return i;
            }
        }
        return -1;
    }

    private void clickDraftByIndex(int index) {
        WebDriverWait outerWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        outerWait.until(d -> {
            try {
                List<WebElement> list = d.findElements(By.cssSelector(BY_FOR_EMAIL_LIST));
                if (index >= list.size()) {
                    System.out.println("clickDraftByIndex: index " + index + " out of bounds, list size=" + list.size());
                    return false;
                }
                WebElement row = list.get(index);
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(row));
                row.click();

                boolean composerAppeared = false;
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.presenceOfElementLocated(
                                    By.cssSelector("[data-testid^=\"composer-\"]")));
                    composerAppeared = true;
                } catch (TimeoutException e) {
                    System.out.println("clickDraftByIndex: composer did not appear after click on index " + index);
                }
                return composerAppeared;

            } catch (StaleElementReferenceException e) {
                System.out.println("clickDraftByIndex: stale element, retrying...");
                return false;
            }
        });
    }

    private boolean checkForSearchedMessage(){
        try {
            waitForComposerToFullyLoad();
            waitForElement(messageAddress);

            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.attributeContains(messageAddress, "title", EMAIL));
            } catch (TimeoutException e) {
                System.out.println("Address never populated with expected email. Current title=["
                        + messageAddress.getAttribute("title") + "]");
                switchToDefaultContent();
                return false;
            }

            waitForElement(subjectField);
            String subj = subjectField.getAttribute("value");
            if (!subj.equals(MAIL_SUBJECT)) {
                System.out.println("Subject mismatch: [" + subj + "]");
                switchToDefaultContent();
                return false;
            }

            switchToIframe();
            waitForElement(messageField);
            String body = messageField.getText();
            if (!body.equals(MAIL_CONTENT)) {
                System.out.println("Body mismatch: [" + body + "]");
                switchToDefaultContent();
                return false;
            }
            switchToDefaultContent();
            return true;

        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("Exception in checkForSearchedMessage: " + e.getMessage());
            switchToDefaultContent();
            return false;
        }
    }

    private boolean checkSentHeader(int index) {
        try {
            String senderTitle = getTitleFromRow(index, "[data-testid=\"message-column:sender-address\"]");
            System.out.println("Row " + index + " sender=[" + senderTitle + "]");
            if (!senderTitle.contains(EMAIL)) { return false; }

            String subjectTitle = getTitleFromRow(index, "[data-testid=\"message-column:subject\"]");
            System.out.println("Row " + index + " subject=[" + subjectTitle + "]");
            return subjectTitle.trim().contains(MAIL_SUBJECT);

        } catch (TimeoutException | NoSuchElementException | IndexOutOfBoundsException e) {
            return false;
        }
    }

    private String getTitleFromRow(int index, String innerSelector) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    List<WebElement> rows = d.findElements(By.cssSelector(BY_FOR_EMAIL_LIST));
                    WebElement row = rows.get(index);
                    WebElement el = row.findElement(By.cssSelector(innerSelector));
                    String title = el.getText();
                    return (title != null && !title.isEmpty()) ? title : null;
                });
    }

    private void waitForSendConfirmationToClear() {
        try {
            // Wait for a send-confirmation toast/notification to appear, then disappear
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("[data-testid=\"notification:success\"]")));

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector("[data-testid=\"notification:success\"]")));
        } catch (TimeoutException e) {
            // Toast might not appear/disappear as expected — don't fail the flow for this
            System.out.println("No send-confirmation toast detected or it didn't disappear in time.");
        }
    }

    private void waitForComposerToFullyLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-testid^=\"composer-\"]")
        ));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid^=\"composer-\"]")
        ));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid=\"composer:address\"]")
        ));
    }

    private void closeMessage() {
        switchToDefaultContent();
        closeComposer();
    }


    private void switchToDefaultContent(){
        driver.switchTo().defaultContent();
    }


    private void switchToIframe() {
        waitForElement(messageIframe);
        driver.switchTo().frame(messageIframe);
    }

    private WebElement getCloseButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-testid^=\"composer-\"]")
        ));

        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid=\"composer:close-button\"]")
                ));
    }


    private void  closeComposer() {
        WebElement closeButton = getCloseButton();
        closeButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("[data-testid^=\"composer-\"]")
                ));
    }

    private void waitForMessageList() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(BY_FOR_EMAIL_LIST)
                ));
    }

    private void clickSendButton() {
        WebElement sendButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(BY_FOR_SEND_BUTTON)));
        sendButton.click();
    }
}
