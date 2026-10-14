package com.epam.training.mateusz_smola.driver;

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

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        logger.debug("Retrieving driver instance for current thread: {}", Thread.currentThread().getName());
        return driver.get();
    }

    public static void setDriver() {

        if (driver.get() != null) {
            logger.warn("Driver already exists for this thread. Quitting existing driver before creating a new one.");
            quitDriver();
        }

        String browser = System.getProperty("browser");
        logger.debug("System property 'browser' resolved to: {}", browser);
        logger.info("ACTION: Initializing WebDriver for browser '{}'", browser);

        switch (browser) {

            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                driver.set(new EdgeDriver(edgeOptions));
                logger.info("Edge driver created successfully on thread: {}", Thread.currentThread().getName());
            }

            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver.set(new FirefoxDriver(firefoxOptions));
                logger.info("Firefox driver created successfully on thread: {}", Thread.currentThread().getName());
            }

            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                driver.set(new ChromeDriver(chromeOptions));
                logger.info("Chrome driver created successfully on thread: {}", Thread.currentThread().getName());
            }

            case null, default -> {
                logger.error("Invalid or missing browser property: '{}'. Expected -Dbrowser=chrome|firefox|edge", browser);
                throw new IllegalArgumentException("No compatible browser specified : -Dbrowser=yourbrowser");
            }
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("ACTION: Quitting WebDriver and removing from ThreadLocal");
            driver.get().quit();
            driver.remove();
        } else {
            logger.debug("quitDriver() called but no active driver found for thread: {}", Thread.currentThread().getName());
        }
    }

}