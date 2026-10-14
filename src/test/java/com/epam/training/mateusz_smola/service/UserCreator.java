package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreator {

    private static final Logger logger = LoggerFactory.getLogger(UserCreator.class);

    private static final String USERNAME = "userdata.name";
    private static final String PASSWORD = "userdata.password";

    public static User CreateUser() {
        logger.debug("Creating User object from test data (keys: {}, {})", USERNAME, PASSWORD);

        String username = DataReader.getData(USERNAME);
        String password = DataReader.getData(PASSWORD);
        User user = new User(username, password);

        logger.debug("User object created for username: {}", username);
        return user;
    }
}