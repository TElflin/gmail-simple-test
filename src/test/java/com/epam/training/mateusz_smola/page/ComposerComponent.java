package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ComposerComponent extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(ComposerComponent.class);

    public static final String BY_FOR_SEND_BUTTON = "[data-testid=\"composer:send-button\"]";
    private static final By COMPOSER_ROOT = By.cssSelector("[data-testid^=\"composer-\"]");
    private static final By COMPOSER_ADDRESS = By.cssSelector("[data-testid=\"composer:address\"]");
    private static final By CLOSE_BUTTON = By.cssSelector("[data-testid=\"composer:close-button\"]");

    @FindBy(css = "[id^=\"to-composer\"]")
    private WebElement addresseeField;

    @FindBy(css = "[id^=\"subject-composer\"]")
    private WebElement subjectField;

    @FindBy(css = "iframe[data-testid=\"rooster-iframe\"]")
    private WebElement messageIframe;

    @FindBy(id = "rooster-editor")
    private WebElement messageField;

    @FindBy(css = "span.composer-addresses-fakefield-inner")
    private WebElement messageAddress;

    public ComposerComponent(WebDriver driver) {
        super(driver);
    }

    public ComposerComponent fillFields(Email email) {
        logger.info("ACTION: Filling compose fields (to: {}, subject: {})",
                email.getEmailAddress(), email.getSubject());

        waitForElement(addresseeField);
        addresseeField.sendKeys(email.getEmailAddress());

        waitForElement(subjectField);
        subjectField.sendKeys(email.getSubject());

        switchToIframe();
        waitForElement(messageField);
        messageField.clear();
        messageField.sendKeys(email.getContent());
        switchToDefaultContent();

        return this;
    }

    public void send() {
        logger.info("ACTION: Sending mail");
        WebElement sendButton = waitForVisible(
                By.cssSelector(BY_FOR_SEND_BUTTON), Duration.ofSeconds(5));
        sendButton.click();
        waitForSendConfirmationToClear();
        switchToDefaultContent();
    }

    public void close() {
        logger.debug("Closing composer");
        switchToDefaultContent();
        WebElement closeButton = getCloseButton();
        closeButton.click();
        waitForInvisible(COMPOSER_ROOT, Duration.ofSeconds(7));
    }

    public boolean matches(Email email) {
        try {
            waitForComposerToFullyLoad();
            waitForElement(messageAddress);

            try {
                new WebDriverWait(driver, DEFAULT_TIMEOUT)
                        .until(ExpectedConditions.attributeContains(
                                messageAddress, "title", email.getEmailAddress()));
            } catch (TimeoutException e) {
                logger.debug("Address never populated. Current title=[{}]",
                        messageAddress.getAttribute("title"));
                switchToDefaultContent();
                return false;
            }

            waitForElement(subjectField);
            if (!subjectField.getAttribute("value").equals(email.getSubject())) {
                logger.debug("Subject mismatch");
                switchToDefaultContent();
                return false;
            }

            switchToIframe();
            waitForElement(messageField);
            boolean bodyMatches = messageField.getText().equals(email.getContent());
            switchToDefaultContent();
            return bodyMatches;

        } catch (TimeoutException | NoSuchElementException e) {
            logger.debug("Exception in matches(): {}", e.getMessage());
            switchToDefaultContent();
            return false;
        }
    }

    private void switchToIframe() {
        logger.debug("Switching to message iframe");
        waitForElement(messageIframe);
        driver.switchTo().frame(messageIframe);
    }

    private WebElement getCloseButton() {
        waitForVisible(COMPOSER_ROOT, DEFAULT_TIMEOUT);
        return waitForClickable(CLOSE_BUTTON, Duration.ofSeconds(5));
    }

    private void waitForComposerToFullyLoad() {
        logger.debug("Waiting for composer to fully load");
        waitForVisible(COMPOSER_ROOT, DEFAULT_TIMEOUT);
        waitForVisible(COMPOSER_ADDRESS, DEFAULT_TIMEOUT);
    }

    private void waitForSendConfirmationToClear() {
        By toast = By.cssSelector("[data-testid=\"notification:success\"]");
        try {
            waitForVisible(toast, Duration.ofSeconds(5));
            waitForInvisible(toast, DEFAULT_TIMEOUT);
        } catch (TimeoutException e) {
            logger.warn("No send-confirmation toast detected");
        }
    }
}
