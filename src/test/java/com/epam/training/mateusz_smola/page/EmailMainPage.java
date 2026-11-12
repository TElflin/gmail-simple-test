package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import com.epam.training.mateusz_smola.service.EmailCreator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static com.epam.training.mateusz_smola.service.EmailCreator.createEmail;

public class EmailMainPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(EmailMainPage.class);

    public static final String BY_FOR_EMAIL_LIST = "div[class=\"item-container-wrapper relative\"]";
    public static final String BY_FOR_SEND_BUTTON = "[data-testid=\"composer:send-button\"]";

    @FindBy(css = "button[data-testid=\"sidebar:compose\"][type=\"button\"]")
    WebElement newMailButton;

    @FindBy(css = "[id^=\"to-composer\"]")
    WebElement addresseeField;

    @FindBy(css = "[id^=\"subject-composer\"]")
    WebElement subjectField;

    @FindBy(css = "iframe[data-testid=\"rooster-iframe\"]")
    WebElement messageIframe;

    @FindBy(id = "rooster-editor")
    WebElement messageField;

    @FindBy(css = "a[data-testid=\"navigation-link:all-drafts\"]")
    WebElement draftPageLink;

    @FindBy(css = "a[data-testid=\"navigation-link:all-sent\"]")
    WebElement sendPageLink;

    @FindBy(css = BY_FOR_EMAIL_LIST)
    List<WebElement> draftedMessages;

    @FindBy(css = "span.composer-addresses-fakefield-inner")
    WebElement messageAddress;

    Email email = createEmail();

    public EmailMainPage(WebDriver driver) {
        super(driver);
    }

    public boolean foundNewMailButton() {
        logger.debug("Checking visibility of 'New Mail' button");
        waitForElement(newMailButton);
        boolean displayed = newMailButton.isDisplayed();
        logger.info("ACTION: 'New Mail' button displayed = {}", displayed);
        return displayed;
    }

    public EmailMainPage createNewDraft() {
        logger.info("ACTION: Creating new draft");
        waitForElement(newMailButton);
        newMailButton.click();
        fillNewMailFields();
        return this;
    }

    public EmailMainPage fillNewMailFields() {
        logger.info("ACTION: Filling new mail fields (to: {}, subject: {})", email.getEmailAddress(), email.getSubject());

        waitForElement(addresseeField);
        addresseeField.sendKeys(email.getEmailAddress());
        logger.debug("Addressee field filled with: {}", email.getEmailAddress());

        waitForElement(subjectField);
        subjectField.sendKeys(email.getSubject());
        logger.debug("Subject field filled with: {}", email.getSubject());

        switchToIframe();
        waitForElement(messageField);
        messageField.clear();
        messageField.sendKeys(email.getContent());
        logger.debug("Message body filled");

        return this;
    }

    public EmailMainPage saveDraft() {
        logger.info("ACTION: Saving draft");
        createNewDraft();
        closeMessage();
        return this;
    }

    public boolean checkForDraft() {
        logger.info("ACTION: Checking for existing matching draft");
        int index = findDraftIndex();
        if (index != -1) {
            logger.debug("Matching draft found at index: {}", index);
            closeComposer();
            return true;
        }
        logger.debug("No matching draft found");
        return false;
    }

    public boolean checkForSent() {
        logger.info("ACTION: Checking for sent message");
        int index = findSentIndex();
        if (index != -1) {
            logger.debug("Matching sent message found at index: {}", index);
            return true;
        }
        logger.debug("No matching sent message found");
        return false;
    }

    public EmailMainPage openDraft() {
        logger.info("ACTION: Opening draft");
        int index = findDraftIndex();
        if (index == -1) {
            logger.error("Matching draft not found - cannot open");
            throw new NoSuchElementException("Matching draft not found");
        }
        logger.debug("Draft opened at index: {}", index);
        return this;
    }

    public EmailMainPage sendMail() {
        logger.info("ACTION: Sending mail");
        clickSendButton();
        waitForSendConfirmationToClear();
        switchToDefaultContent();
        logger.info("Mail sent successfully");
        return this;
    }

    private int findDraftIndex() {
        logger.debug("Searching for matching draft among drafted messages");
        waitForElement(draftPageLink);
        draftPageLink.click();
        waitForMessageList();

        int listSize = draftedMessages.size();
        logger.debug("Found {} drafted messages to check", listSize);

        for (int i = 0; i < listSize; i++) {
            logger.debug("Checking draft at index: {}", i);
            clickDraftByIndex(i);
            if (checkForSearchedMessage()) {
                logger.debug("Match found at index: {}", i);
                return i;
            }
            switchToDefaultContent();
            closeComposer();

            draftPageLink.click();
            waitForMessageList();
        }
        logger.debug("No matching draft found after checking {} messages", listSize);
        return -1;
    }

    private int findSentIndex() {
        logger.debug("Searching for matching sent message");
        waitForElement(sendPageLink);
        sendPageLink.click();
        waitForMessageList();

        int listSize = draftedMessages.size();
        logger.debug("Found {} sent messages to check", listSize);

        for (int i = 0; i < listSize; i++) {
            if (checkSentHeader(i)) {
                logger.debug("Match found at index: {}", i);
                return i;
            }
        }
        logger.debug("No matching sent message found after checking {} messages", listSize);
        return -1;
    }

    private void clickDraftByIndex(int index) {
        WebDriverWait outerWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        outerWait.until(d -> {
            try {
                List<WebElement> list = d.findElements(By.cssSelector(BY_FOR_EMAIL_LIST));
                if (index >= list.size()) {
                    logger.warn("clickDraftByIndex: index {} out of bounds, list size={}", index, list.size());
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
                    logger.debug("clickDraftByIndex: composer did not appear after click on index {}", index);
                }
                return composerAppeared;

            } catch (StaleElementReferenceException e) {
                logger.debug("clickDraftByIndex: stale element, retrying...");
                return false;
            }
        });
    }

    private boolean checkForSearchedMessage() {
        try {
            waitForComposerToFullyLoad();
            waitForElement(messageAddress);

            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.attributeContains(messageAddress, "title", email.getEmailAddress()));
            } catch (TimeoutException e) {
                logger.debug("Address never populated with expected email. Current title=[{}]",
                        messageAddress.getAttribute("title"));
                switchToDefaultContent();
                return false;
            }

            waitForElement(subjectField);
            String subj = subjectField.getAttribute("value");
            if (!subj.equals(email.getSubject())) {
                logger.debug("Subject mismatch: [{}]", subj);
                switchToDefaultContent();
                return false;
            }

            switchToIframe();
            waitForElement(messageField);
            String body = messageField.getText();
            if (!body.equals(email.getContent())) {
                logger.debug("Body mismatch: [{}]", body);
                switchToDefaultContent();
                return false;
            }
            switchToDefaultContent();
            return true;

        } catch (TimeoutException | NoSuchElementException e) {
            logger.debug("Exception in checkForSearchedMessage: {}", e.getMessage());
            switchToDefaultContent();
            return false;
        }
    }

    private boolean checkSentHeader(int index) {
        try {
            String senderTitle = getTitleFromRow(index, "[data-testid=\"message-column:sender-address\"]");
            logger.debug("Row {} sender=[{}]", index, senderTitle);
            if (!senderTitle.contains(email.getEmailAddress())) {
                return false;
            }

            String subjectTitle = getTitleFromRow(index, "[data-testid=\"message-column:subject\"]");
            logger.debug("Row {} subject=[{}]", index, subjectTitle);
            return subjectTitle.trim().contains(email.getSubject());

        } catch (TimeoutException | NoSuchElementException | IndexOutOfBoundsException e) {
            logger.debug("Exception in checkSentHeader for index {}: {}", index, e.getMessage());
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
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("[data-testid=\"notification:success\"]")));

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector("[data-testid=\"notification:success\"]")));
        } catch (TimeoutException e) {
            logger.warn("No send-confirmation toast detected or it didn't disappear in time");
        }
    }

    private void waitForComposerToFullyLoad() {
        logger.debug("Waiting for composer to fully load");
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

    private void switchToDefaultContent() {
        logger.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }

    private void switchToIframe() {
        logger.debug("Switching to message iframe");
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

    private void closeComposer() {
        logger.debug("Closing composer");
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
        logger.debug("Clicking send button");
        WebElement sendButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(BY_FOR_SEND_BUTTON)));
        sendButton.click();
    }
}