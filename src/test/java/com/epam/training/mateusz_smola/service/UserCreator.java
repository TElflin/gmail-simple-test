package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.User;

public class UserCreator {
    private static final String USERNAME = "selenium.test.epam";
    private static final String PASSWORD = "Selenium123!";

    public static User CreateUser(){
        return new User(USERNAME, PASSWORD);
    }
}
