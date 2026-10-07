package com.epam.training.mateusz_smola.driver;

import com.epam.training.mateusz_smola.util.TestListener;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Listeners;


public class DriverManager {


    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver() {

        if (driver.get() != null) {
            quitDriver();
        }

        String browser = System.getProperty("browser");
        switch (browser){

            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                driver.set(new EdgeDriver(edgeOptions));
            }

            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver.set(new FirefoxDriver(firefoxOptions));
            }

            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                driver.set(new ChromeDriver(chromeOptions));
            }

            case null, default -> {
                throw new IllegalArgumentException("No compatible browser specified : -Dbrowser=yourbrowser");
            }
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

}
