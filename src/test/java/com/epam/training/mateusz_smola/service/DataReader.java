package com.epam.training.mateusz_smola.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DataReader {

    private static final Logger logger = LoggerFactory.getLogger(DataReader.class);

    private static final ResourceBundle resource;

    static {
        String environment = System.getProperty("environment");
        logger.info("ACTION: Loading test data resource bundle for environment: {}", environment);

        try {
            resource = ResourceBundle.getBundle(environment);
            logger.info("Resource bundle loaded successfully for environment: {}", environment);
        } catch (Exception e) {
            logger.error("Failed to load resource bundle for environment: '{}'. " +
                    "Make sure -Denvironment=yourEnv is set and the properties file exists.", environment, e);
            throw e;
        }
    }
    public static String getData(String key) {
        logger.debug("Retrieving data for key: {}", key);
        String value = resource.getString(key);
        logger.debug("Value retrieved for key '{}': {}", key, key.toLowerCase().contains("password") ? "****" : value);
        return value;
    }
}