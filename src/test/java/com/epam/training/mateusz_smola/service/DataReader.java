package com.epam.training.mateusz_smola.service;

import java.util.ResourceBundle;

public class DataReader {
    private static ResourceBundle resource = ResourceBundle.getBundle(System.getProperty("environment"));

    public static String getData(String key ){
        return resource.getString(key);
    }
}
