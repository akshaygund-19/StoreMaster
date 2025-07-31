package com.akshay.StoreMaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cartItem_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_ID", nullable = false)
    private Product product;

    private Integer quantity;

    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
