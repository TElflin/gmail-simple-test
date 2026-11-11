package com.epam.training.mateusz_smola.util;

import com.epam.training.mateusz_smola.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("========== STARTING TEST: {} ==========", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("TEST PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("TEST FAILED: {} - Reason: {}", testName,
                result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");

        WebDriver driver = DriverManager.getInstance().getDriver();
        ScreenshotUtil.saveScreenshot(driver, testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("TEST SKIPPED: {}", result.getMethod().getMethodName());
    }
}