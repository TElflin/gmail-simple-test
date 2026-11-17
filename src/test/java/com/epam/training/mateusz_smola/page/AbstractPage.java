package com.epam.training.mateusz_smola.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPage.class);

    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

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
        logger.debug("Waiting up to {}s for element visibility: {}", DEFAULT_TIMEOUT.getSeconds(), element);
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become visible within timeout: {}", element, e);
            throw e;
        }
    }

    protected WebElement waitForVisible(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForInvisible(By locator, Duration timeout) {
        new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void switchToDefaultContent() {
        logger.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }
}