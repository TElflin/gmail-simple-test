package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.Email;
import com.epam.training.mateusz_smola.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.epam.training.mateusz_smola.service.UserCreator.CreateUser;

public class EmailCreator {

    private static final Logger logger = LoggerFactory.getLogger(EmailCreator.class);

    private static final String PAGE_URL = "https://account.proton.me/pl/mail";

    public static Email createEmail() {
        User user = CreateUser();
        logger.debug("Creating Email test object for user: {} with pageUrl: {}", user.getUsername(), PAGE_URL);
        return new Email(user, PAGE_URL);
    }
}