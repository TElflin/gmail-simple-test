package com.epam.training.mateusz_smola.driver.factory;

public class DriverProvider {
    public static DriverFactory getFactory(String browser) {
        return switch (browser) {
            case "chrome" -> new ChromeDriverFactory();
            case "firefox" -> new FirefoxDriverFactory();
            case "edge" -> new EdgeDriverFactory();
            case null, default -> throw new IllegalArgumentException(
                    "No compatible browser specified : -Dbrowser=yourbrowser");
        };
    }
}
