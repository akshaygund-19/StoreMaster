package com.akshay.StoreMaster.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponseDTO {
    private List<CartItemDTO> items;
    private double totalPrice;

    public CartResponseDTO(List<CartItemDTO> items, double totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

}
