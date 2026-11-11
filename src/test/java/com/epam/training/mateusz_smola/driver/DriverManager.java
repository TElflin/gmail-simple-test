package com.epam.training.mateusz_smola.driver;

import com.epam.training.mateusz_smola.driver.factory.DriverFactory;
import com.epam.training.mateusz_smola.driver.factory.DriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<DriverManager> instanceHolder = new ThreadLocal<>();

    private WebDriver driver;

    private DriverManager() {}

    public static DriverManager getInstance() {
        DriverManager instance = instanceHolder.get();

        if (instance == null) {
            instance = new DriverManager();
            instanceHolder.set(instance);
        }
        return instance;
    }


    public WebDriver getDriver() {
        logger.debug("Retrieving driver instance for thread: {}", Thread.currentThread().getName());
        return driver;
    }


    public void setDriver() {
        if (driver != null) {
            logger.warn("Driver already exists for this thread. Quitting existing driver first.");
            quitDriver();
        }

        String browser = System.getProperty("browser");
        logger.info("ACTION: Initializing WebDriver for browser '{}'", browser);

        DriverFactory factory = DriverProvider.getFactory(browser);
        WebDriver rawDriver = factory.createDriver();

        driver = new WebDriverDecorator(rawDriver);

        logger.info("{} driver created successfully on thread: {}", browser, Thread.currentThread().getName());
    }

    public void quitDriver() {
        if (driver != null) {
            logger.info("ACTION: Quitting WebDriver and clearing instance");
            driver.quit();
            driver = null;
            instanceHolder.remove();
        } else {
            logger.debug("quitDriver() called but no active driver found for thread: {}", Thread.currentThread().getName());
        }
    }

}