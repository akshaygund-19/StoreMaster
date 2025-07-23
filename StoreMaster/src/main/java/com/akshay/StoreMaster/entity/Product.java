package com.akshay.StoreMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer product_ID;

    private String name;

    private String description;

    private int price;

    private int stock_quantity;

    private String category;

    private LocalDateTime created_at;
}
