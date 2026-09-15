package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.Email;

import static com.epam.training.mateusz_smola.service.UserCreator.CreateUser;

public class EmailCreator {
    private static final String PAGE_URL = "https://account.proton.me/pl/mail";

    public static Email createEmail() {
        return new Email(CreateUser(),PAGE_URL);

    }
}
