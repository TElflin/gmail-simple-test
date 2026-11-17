
package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class DraftsPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(DraftsPage.class);

    public static final String BY_FOR_EMAIL_LIST = "div[class=\"item-container-wrapper relative\"]";

    @FindBy(css = "a[data-testid=\"navigation-link:all-drafts\"]")
    private WebElement draftPageLink;

    @FindBy(css = BY_FOR_EMAIL_LIST)
    private List<WebElement> draftedMessages;

    private final ComposerComponent composer;

    public DraftsPage(WebDriver driver) {
        super(driver);
        this.composer = new ComposerComponent(driver);
    }

    public boolean containsDraft(Email email) {
        int index = findDraftIndex(email);
        if (index != -1) {
            composer.close();
            return true;
        }
        return false;
    }

    public ComposerComponent openDraft(Email email) {
        int index = findDraftIndex(email);
        if (index == -1) {
            logger.error("Matching draft not found - cannot open");
            throw new NoSuchElementException("Matching draft not found");
        }
        logger.debug("Draft opened at index: {}", index);
        return composer;
    }

    private int findDraftIndex(Email email) {
        logger.debug("Searching for matching draft");
        waitForElement(draftPageLink);
        draftPageLink.click();
        waitForMessageList();

        int listSize = draftedMessages.size();
        logger.debug("Found {} drafted messages to check", listSize);

        for (int i = 0; i < listSize; i++) {
            clickDraftByIndex(i);
            if (composer.matches(email)) {
                logger.debug("Match found at index: {}", i);
                return i;
            }
            switchToDefaultContent();
            composer.close();
            draftPageLink.click();
            waitForMessageList();
        }
        return -1;
    }

    private void clickDraftByIndex(int index) {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
            try {
                List<WebElement> list = d.findElements(By.cssSelector(BY_FOR_EMAIL_LIST));
                if (index >= list.size()) {
                    return false;
                }
                WebElement row = list.get(index);
                waitForClickable(By.cssSelector(BY_FOR_EMAIL_LIST), Duration.ofSeconds(5));
                row.click();

                try {
                    waitForVisible(By.cssSelector("[data-testid^=\"composer-\"]"),
                            Duration.ofSeconds(5));
                    return true;
                } catch (TimeoutException e) {
                    return false;
                }
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    private void waitForMessageList() {
        waitForClickable(By.cssSelector(BY_FOR_EMAIL_LIST), DEFAULT_TIMEOUT);
    }
}