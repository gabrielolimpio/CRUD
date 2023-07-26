package com.example.CRUD.services.exceptions;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String msg) {
        // repassa o argumento para a super classe
        super(msg);
    }
}
