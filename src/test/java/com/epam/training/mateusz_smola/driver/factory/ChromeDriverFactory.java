package com.epam.training.mateusz_smola.driver.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory implements DriverFactory {

    @Override
    public WebDriver createDriver(){
        return new ChromeDriver(new ChromeOptions());
    }
}
