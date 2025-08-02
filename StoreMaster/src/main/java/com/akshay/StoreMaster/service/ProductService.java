package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.dto.ProductRequestDTO;
import com.akshay.StoreMaster.dto.ProductResponseDTO;
import com.akshay.StoreMaster.entity.Product;
import com.akshay.StoreMaster.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    //This method validates product input, saves the product to the database, and returns the saved product details as a response DTO
    public List<ProductResponseDTO> addProduct(List<ProductRequestDTO> dtoList) {
        List<ProductResponseDTO> listOfProductsToAdd = new ArrayList<>();

        for (ProductRequestDTO productRequestDTO : dtoList) {
            if (productRequestDTO.getName() == null || productRequestDTO.getName().isBlank()) {
                throw new IllegalArgumentException("Name cannot blank");
            } else if (productRequestDTO.getPrice() < 0) {
                throw new IllegalArgumentException("Price should be positive");
            } else if (productRequestDTO.getStock_quantity() < 0) {
                throw new IllegalArgumentException("Quantity should be positive");
            }
                Product product = new Product();
                product.setName(productRequestDTO.getName());
                product.setPrice(productRequestDTO.getPrice());
                product.setDescription(productRequestDTO.getDescription());
                product.setStock_quantity(productRequestDTO.getStock_quantity());
                product.setCategory(productRequestDTO.getCategory());
                product.setCreated_at(LocalDateTime.now());
                Product savedProduct = productRepository.save(product);

                ProductResponseDTO responseDTO = new ProductResponseDTO();
                responseDTO.setProduct_ID(savedProduct.getProduct_Id());
                responseDTO.setName(savedProduct.getName());
                responseDTO.setPrice(savedProduct.getPrice());
                responseDTO.setDescription(savedProduct.getDescription());
                responseDTO.setCategory(savedProduct.getCategory());
                responseDTO.setStock_quantity(savedProduct.getStock_quantity());
                listOfProductsToAdd.add(responseDTO);
        }
        return listOfProductsToAdd;
    }

    //This method updates an existing product’s details after validating input, saving changes to the database, and returning updated product information
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Optional<Product> checkProduct = productRepository.findById(productId);
        if (checkProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found ");
        }

        Product product = checkProduct.get();

        if (productRequestDTO.getName() != null || !productRequestDTO.getName().isBlank()) {
            product.setName(productRequestDTO.getName());
        }
        if (productRequestDTO.getPrice() >= 0) {
            product.setPrice(productRequestDTO.getPrice());
        }
        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
        }
        if (productRequestDTO.getStock_quantity() >= 0) {
            product.setStock_quantity(productRequestDTO.getStock_quantity());
        }
        if (productRequestDTO.getCategory() != null) {
            product.setCategory(productRequestDTO.getCategory());
        }
        product.setCreated_at(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProduct_ID(savedProduct.getProduct_Id());
        responseDTO.setName(savedProduct.getName());
        responseDTO.setPrice(savedProduct.getPrice());
        responseDTO.setDescription(savedProduct.getDescription());
        responseDTO.setCategory(savedProduct.getCategory());
        responseDTO.setStock_quantity(savedProduct.getStock_quantity());
        return responseDTO;
    }

    public void deleteProduct(Long productId){
        Optional<Product> checkProduct = productRepository.findById(productId);
        if (checkProduct.isEmpty()){
            throw new IllegalArgumentException("Product not found ");
        }
        checkProduct.ifPresent(productRepository::delete);
    }

    public List<ProductResponseDTO> getAllProducts(){
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream().map(product -> {

            ProductResponseDTO responseDTO = new ProductResponseDTO();

            responseDTO.setProduct_ID(product.getProduct_Id());
            responseDTO.setPrice(product.getPrice());
            responseDTO.setStock_quantity(product.getStock_quantity());
            responseDTO.setName(product.getName());
            responseDTO.setDescription(product.getDescription());
            responseDTO.setStock_quantity(product.getStock_quantity());
            return responseDTO;
        }).collect(Collectors.toList());
    }

    public ProductResponseDTO getProduct(Long productId){
       Optional<Product> product = productRepository.findById(productId);

       if (product.isEmpty()){
           throw new IllegalArgumentException("Product Not found: ID{}" + productId);
       }

       Product foundProduct = product.get();
       ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProduct_ID(foundProduct.getProduct_Id());
        responseDTO.setName(foundProduct.getName());
        responseDTO.setPrice(foundProduct.getPrice());
        responseDTO.setDescription(foundProduct.getDescription());
        responseDTO.setCategory(foundProduct.getCategory());
        responseDTO.setStock_quantity(foundProduct.getStock_quantity());
        return responseDTO;
    }
}