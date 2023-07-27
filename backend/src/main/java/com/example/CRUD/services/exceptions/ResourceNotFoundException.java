package com.example.CRUD.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msg) {
        // repassa o argumento para a super classe
        super(msg);
    }
}
