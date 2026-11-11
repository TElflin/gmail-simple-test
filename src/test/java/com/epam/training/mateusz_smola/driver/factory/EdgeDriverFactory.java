package com.epam.training.mateusz_smola.driver.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverFactory implements DriverFactory{
    @Override
    public WebDriver createDriver() {
        return new EdgeDriver(new EdgeOptions());
    }
}
