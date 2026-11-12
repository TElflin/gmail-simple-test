package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EmailCreator {

    private static final Logger logger = LoggerFactory.getLogger(EmailCreator.class);

    private static final String EMAIL = "you.are@beautiful.pl";
    private static final String MAIL_SUBJECT = "Keep smiling";
    private static final String MAIL_CONTENT = "Keep going \n" +
            "Keep being cool";

    public static Email createEmail() {
        logger.debug("Creating Email test object containing  address: {} with subject: {} and content: {}", EMAIL, MAIL_SUBJECT,MAIL_CONTENT);
        return new Email(EMAIL, MAIL_SUBJECT, MAIL_CONTENT);
    }
}