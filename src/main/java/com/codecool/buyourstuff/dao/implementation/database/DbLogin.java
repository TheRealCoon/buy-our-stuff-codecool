package com.codecool.buyourstuff.dao.implementation.database;

public class DbLogin {
    static final String URL = "jdbc:postgresql://localhost:5432/bos_webshop";
    static final String USER = System.getenv("USER");
    static final String PASSWORD = System.getenv("PASSWORD");

    private DbLogin() {}
}
