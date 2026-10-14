package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPage.class);

    protected WebDriver driver;

    protected AbstractPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        logger.debug("Page object initialized: {}", this.getClass().getSimpleName());
    }

    protected void openPage(String pageUrl) {
        logger.info("ACTION: Opening page URL: {}", pageUrl);
        driver.get(pageUrl);
    }

    protected void waitForElement(WebElement element) {
        logger.debug("Waiting up to 10s for element visibility: {}", element);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become visible within timeout: {}", element, e);
            throw e;
        }
    }

}