package com.example.CRUD.services;

import com.example.CRUD.dto.ProductDTO;
import com.example.CRUD.entities.Product;
import com.example.CRUD.repositories.ProductRepository;
import com.example.CRUD.services.exceptions.DatabaseException;
import com.example.CRUD.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);

        // return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
        // equivalente a
//        return list.stream().map(ProductDTO::new).collect(Collectors.toList());

//        agora com paginacao, nao precisa do stream() nem o collect()
//        return list.map(x -> new ProductDTO(x));
        return list.map(ProductDTO::new);

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
//      Product entity = obj.get(); não trata exceção
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional(readOnly = true)
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
//        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional(readOnly = true)
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
//            Product entity = repository.getOne(id); metodo deprecated
//            Product entity = repository.getById(id); metodo deprecated
//            Product entity = repository.getReferenceById(id);  tb não funciona como esperado

            @SuppressWarnings("deprecation")
            Product entity = repository.getOne(id);
//            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new ProductDTO(entity);
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
