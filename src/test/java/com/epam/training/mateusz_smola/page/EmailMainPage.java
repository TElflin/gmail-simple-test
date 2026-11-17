package com.epam.training.mateusz_smola.page;

import com.epam.training.mateusz_smola.model.Email;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailMainPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(EmailMainPage.class);

    @FindBy(css = "button[data-testid=\"sidebar:compose\"][type=\"button\"]")
    private WebElement newMailButton;

    private final ComposerComponent composer;
    private final DraftsPage draftsPage;
    private final SentPage sentPage;

    public EmailMainPage(WebDriver driver) {
        super(driver);
        this.composer = new ComposerComponent(driver);
        this.draftsPage = new DraftsPage(driver);
        this.sentPage = new SentPage(driver);
    }

    public boolean foundNewMailButton() {
        waitForElement(newMailButton);
        boolean displayed = newMailButton.isDisplayed();
        logger.info("ACTION: 'New Mail' button displayed = {}", displayed);
        return displayed;
    }

    public EmailMainPage saveDraft(Email email) {
        logger.info("ACTION: Saving draft");
        waitForElement(newMailButton);
        newMailButton.click();
        composer.fillFields(email);
        composer.close();
        return this;
    }

    public boolean checkForDraft(Email email) {
        return draftsPage.containsDraft(email);
    }

    public boolean checkForSent(Email email) {
        return sentPage.containsSentMail(email);
    }

    public EmailMainPage sendDraft(Email email) {
        logger.info("ACTION: Opening and sending draft");
        draftsPage.openDraft(email).send();
        return this;
    }
}