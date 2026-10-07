package com.epam.training.mateusz_smola.util;

import com.epam.training.mateusz_smola.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure (ITestResult iTestResult){
        WebDriver driver = DriverManager.getDriver();
        String testName = iTestResult.getMethod().getMethodName();
        ScreenshotUtil.saveScreenshot(driver, testName);
    }
}
