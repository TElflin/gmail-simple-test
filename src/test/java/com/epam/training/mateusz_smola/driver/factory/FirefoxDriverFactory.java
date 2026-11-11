package com.epam.training.mateusz_smola.driver.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverFactory implements DriverFactory {
    @Override
    public WebDriver createDriver() {
        return new FirefoxDriver(new FirefoxOptions());
    }
}
