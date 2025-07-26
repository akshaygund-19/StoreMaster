package com.akshay.StoreMaster.controller;

import com.akshay.StoreMaster.dto.ProductRequestDTO;
import com.akshay.StoreMaster.dto.ProductResponseDTO;
import com.akshay.StoreMaster.entity.Product;
import com.akshay.StoreMaster.repository.ProductRepository;
import com.akshay.StoreMaster.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storeMaster/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    //This endpoint handles adding a new product by accepting product details, delegating to the service layer, and returning the saved product information.
    @Transactional
    @PostMapping("/add")
    public ResponseEntity<List<ProductResponseDTO>> add(@RequestBody List<ProductRequestDTO> dtoList){
        List<ProductResponseDTO> product = productService.addProduct(dtoList);
        log.info("Product added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable int productId, @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO updateProduct = productService.updateProduct(productId, productRequestDTO);
        log.info("Product updated successfully. Id:{}", productId);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> delete(@PathVariable int productId){
         productService.deleteProduct(productId);
        log.info("Product deleted Successfully ID:{}",productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllProduct")
    public List<ProductResponseDTO> getAllProducts(){
        return productService.getAllProducts();
    }
}
