package com.epam.training.mateusz_smola.driver;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverDecorator implements WebDriver, TakesScreenshot {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverDecorator.class);

    private final WebDriver decoratedDriver;

    public WebDriverDecorator(WebDriver driver) {
        this.decoratedDriver = driver;
    }

    @Override
    public void get(String url) {
        logger.info("ACTION: Navigating to URL: {}", url);
        decoratedDriver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return decoratedDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return decoratedDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        logger.debug("Finding elements by: {}", by);
        return decoratedDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        logger.debug("Finding element by: {}", by);
        return decoratedDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return decoratedDriver.getPageSource();
    }

    @Override
    public void close() {
        logger.info("ACTION: Closing current browser window/tab");
        decoratedDriver.close();
    }

    @Override
    public void quit() {
        logger.info("ACTION: Quitting browser session");
        decoratedDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return decoratedDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return decoratedDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return decoratedDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return decoratedDriver.navigate();
    }

    @Override
    public Options manage() {
        return decoratedDriver.manage();
    }


    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) decoratedDriver).getScreenshotAs(target);
    }


}
