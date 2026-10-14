package com.epam.training.mateusz_smola.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static void saveScreenshot(WebDriver driver, String testName) {
        logger.debug("Capturing screenshot for test: {}", testName);
        File screenCapture = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            String path = ".//target/screenshots/"
                    + testName
                    + "_"
                    + getTimeStamp()
                    + ".png";

            File destFile = new File(path);
            FileUtils.copyFile(screenCapture, destFile);
            logger.info("Screenshot saved at: {}", destFile.getAbsolutePath());

        } catch (IOException e) {
            logger.error("Failed to save screenshot for test '{}': {}", testName, e.getMessage(), e);
        }
    }

    private static String getTimeStamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu.MM.dd_HH-mm-ss");
        return ZonedDateTime.now().format(dateTimeFormatter);
    }
}