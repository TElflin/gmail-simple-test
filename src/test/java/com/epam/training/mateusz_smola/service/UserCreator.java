package com.epam.training.mateusz_smola.service;

import com.epam.training.mateusz_smola.model.User;
import com.epam.training.mateusz_smola.service.DataReader;

public class UserCreator {
    private static final String USERNAME = "userdata.name";
    private static final String PASSWORD = "userdata.password";

    public static User CreateUser(){
        return new User(DataReader.getData(USERNAME), DataReader.getData(PASSWORD));
    }
}
