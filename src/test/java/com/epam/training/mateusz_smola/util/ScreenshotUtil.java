package com.epam.training.mateusz_smola.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void saveScreenshot(WebDriver driver, String testName) {
        File screenCapture = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
                String path = ".//target/screenshots/"
                        + testName
                        + getTimeStamp()
                        + ".png";
            FileUtils.copyFile(screenCapture, new File(path));
        } catch (IOException e){

        }

    }

    private static String getTimeStamp(){
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("uuuu.MM.dd_HH-mm-ss");
        return ZonedDateTime.now().format(dateTimeFormatter);
    }
}
