package com.akshay.StoreMaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductResponseDTO {
    private int product_ID;

    private String name;

    private String description;

    private Integer price;

    private Integer stock_quantity;

    private String category;
}
