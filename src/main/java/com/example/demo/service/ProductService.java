package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.UpdateStockDto;
import com.example.demo.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity add(ProductDto request) {
        ProductEntity product = new ProductEntity();
        product.setName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        //todo: save into db
        return productRepository.save(product);
    }

    public List<ProductEntity> fetchByPrice(boolean isByPrice, long minPrice) {
        if (isByPrice){
            return fetchUnderPrice(minPrice);
        }
        else {
            return fetchAll();
        }
    }

    public List<ProductEntity> fetchUnderPrice(long minPrice) {
        return productRepository.findByPriceLessThanEqual(minPrice);
    }

    public List<ProductEntity> fetch(boolean isInStock){
        if (isInStock){
            return fetchAllInStock();
        }
        else {
            return fetchAll();
        }
    }

    public List<ProductEntity> fetchAll() {
        return (List<ProductEntity>) productRepository.findAll();
    }

    public List<ProductEntity> fetchAllInStock() {
        return productRepository.findByStockGreaterThan(0);
    }

    public void delete(long id){
        productRepository.deleteById(id);
    }

    public ProductEntity getById(long id) {
        return productRepository.findById(id).orElse(new ProductEntity());
    }

    public ProductEntity updateStock(UpdateStockDto request){
        //get product by id from database
        ProductEntity product = getById(request.getId());

        //update stock
        product.setStock(product.getStock() + request.getNumberOfStock());

        //save updated product to database
        return productRepository.save(product); //save() can be used to create or update stock
    }
}
