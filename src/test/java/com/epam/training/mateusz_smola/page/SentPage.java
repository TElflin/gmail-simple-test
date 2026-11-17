package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SentPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(SentPage.class);

    public static final String BY_FOR_EMAIL_LIST = "div[class=\"item-container-wrapper relative\"]";

    @FindBy(css = "a[data-testid=\"navigation-link:all-sent\"]")
    private WebElement sendPageLink;

    @FindBy(css = BY_FOR_EMAIL_LIST)
    private List<WebElement> sentMessages;

    public SentPage(WebDriver driver) {
        super(driver);
    }

    public boolean containsSentMail(Email email) {
        logger.info("ACTION: Checking for sent message");
        waitForElement(sendPageLink);
        sendPageLink.click();
        waitForMessageList();

        int listSize = sentMessages.size();
        for (int i = 0; i < listSize; i++) {
            if (matchesHeader(i, email)) {
                logger.debug("Match found at index: {}", i);
                return true;
            }
        }
        return false;
    }

    private boolean matchesHeader(int index, Email email) {
        try {
            String sender = getTitleFromRow(index,
                    "[data-testid=\"message-column:sender-address\"]");
            if (!sender.contains(email.getEmailAddress())) {
                return false;
            }
            String subject = getTitleFromRow(index,
                    "[data-testid=\"message-column:subject\"]");
            return subject.trim().contains(email.getSubject());

        } catch (TimeoutException | NoSuchElementException | IndexOutOfBoundsException e) {
            logger.debug("Exception in matchesHeader for index {}: {}", index, e.getMessage());
            return false;
        }
    }

    private String getTitleFromRow(int index, String innerSelector) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    List<WebElement> rows = d.findElements(By.cssSelector(BY_FOR_EMAIL_LIST));
                    WebElement el = rows.get(index).findElement(By.cssSelector(innerSelector));
                    String title = el.getText();
                    return (title != null && !title.isEmpty()) ? title : null;
                });
    }

    private void waitForMessageList() {
        waitForClickable(By.cssSelector(BY_FOR_EMAIL_LIST), DEFAULT_TIMEOUT);
    }
}