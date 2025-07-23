package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.dto.ProductRequestDTO;
import com.akshay.StoreMaster.dto.ProductResponseDTO;
import com.akshay.StoreMaster.entity.Product;
import com.akshay.StoreMaster.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    //This method validates product input, saves the product to the database, and returns the saved product details as a response DTO
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        if (productRequestDTO.getName() == null || productRequestDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot blank");
        } else if (productRequestDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Price should be positive");
        } else if (productRequestDTO.getStock_quantity() < 0) {
            throw new IllegalArgumentException("Quantity should be positive");
        } else {
            product.setName(productRequestDTO.getName());
            product.setPrice(productRequestDTO.getPrice());
            product.setDescription(productRequestDTO.getDescription());
            product.setStock_quantity(productRequestDTO.getStock_quantity());
            product.setCategory(productRequestDTO.getCategory());
            product.setCreated_at(LocalDateTime.now());
            Product savedProduct = productRepository.save(product);

            ProductResponseDTO responseDTO = new ProductResponseDTO();
            responseDTO.setProduct_ID(savedProduct.getProduct_ID());
            responseDTO.setName(savedProduct.getName());
            responseDTO.setPrice(savedProduct.getPrice());
            responseDTO.setDescription(savedProduct.getDescription());
            responseDTO.setCategory(savedProduct.getCategory());
            responseDTO.setStock_quantity(savedProduct.getStock_quantity());
            return responseDTO;
        }
    }

    //This method updates an existing product’s details after validating input, saving changes to the database, and returning updated product information
    public ProductResponseDTO updateProduct(int productId, ProductRequestDTO productRequestDTO) {
        Product checkProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found "));
        if (productRequestDTO.getName() == null || productRequestDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot blank");
        }
        if (productRequestDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Price should be positive");
        }
        if (productRequestDTO.getStock_quantity() < 0) {
            throw new IllegalArgumentException("Quantity should be positive");
        }
        checkProduct.setName(productRequestDTO.getName());
        checkProduct.setDescription(productRequestDTO.getDescription());
        checkProduct.setPrice(productRequestDTO.getPrice());
        checkProduct.setStock_quantity(productRequestDTO.getStock_quantity());
        checkProduct.setCategory(productRequestDTO.getCategory());

        Product savedProduct = productRepository.save(checkProduct);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProduct_ID(savedProduct.getProduct_ID());
        responseDTO.setName(savedProduct.getName());
        responseDTO.setPrice(savedProduct.getPrice());
        responseDTO.setDescription(savedProduct.getDescription());
        responseDTO.setCategory(savedProduct.getCategory());
        responseDTO.setStock_quantity(savedProduct.getStock_quantity());
        return responseDTO;
    }
}


