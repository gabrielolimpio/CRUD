package com.example.CRUD.services;

import com.example.CRUD.entities.Category;
import com.example.CRUD.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
    public List<Category> findAll(){
        return repository.findAll();
    }
}