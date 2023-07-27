package com.example.CRUD.services;

import com.example.CRUD.dto.CategoryDTO;
import com.example.CRUD.entities.Category;
import com.example.CRUD.services.exceptions.DatabaseException;
import com.example.CRUD.services.exceptions.ResourceNotFoundException;
import com.example.CRUD.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);

        // return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
        // equivalente a
//        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());

//        agora com paginacao, nao precisa do stream() nem o collect()
//        return list.map(x -> new CategoryDTO(x));
        return list.map(CategoryDTO::new);

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
//      Category entity = obj.get(); não trata exceção
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional(readOnly = true)
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional(readOnly = true)
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
//            Category entity = repository.getOne(id); metodo deprecated
//            Category entity = repository.getById(id); metodo deprecated
//            Category entity = repository.getReferenceById(id);  tb não funciona como esperado

            @SuppressWarnings("deprecation")
            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
