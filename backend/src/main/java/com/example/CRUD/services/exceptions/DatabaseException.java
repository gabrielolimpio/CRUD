package com.example.CRUD.services.exceptions;

public class DatabaseException extends RuntimeException{

    public DatabaseException(String msg) {
        // repassa o argumento para a super classe
        super(msg);
    }
}
