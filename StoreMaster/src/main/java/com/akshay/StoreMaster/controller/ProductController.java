package com.akshay.StoreMaster.controller;

import com.akshay.StoreMaster.dto.ProductRequestDTO;
import com.akshay.StoreMaster.dto.ProductResponseDTO;
import com.akshay.StoreMaster.repository.ProductRepository;
import com.akshay.StoreMaster.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/storeMaster/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    //This endpoint handles adding a new product by accepting product details, delegating to the service layer, and returning the saved product information.
    @PostMapping("/add")
    public ResponseEntity<ProductResponseDTO> add(@RequestBody ProductRequestDTO productRequestDTO){
        var product = productService.addProduct(productRequestDTO);
        log.info("Product added successful {}", productRequestDTO.getName());
        return ResponseEntity.ok(product);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable int productId, @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO updateProduct = productService.updateProduct(productId, productRequestDTO);
        log.info("Product updated successfully Id:{}", productId);
        return ResponseEntity.ok(updateProduct);
    }
}
