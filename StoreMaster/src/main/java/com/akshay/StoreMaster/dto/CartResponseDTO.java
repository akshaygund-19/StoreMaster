package com.akshay.StoreMaster.dto;

import java.util.List;

public class CartResponseDTO {
    private int cartId;
    private int userId;
    private double totalPrice;
    private List<CartItemDTO> items;
}
