package com.akshay.StoreMaster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductRequestDTO {

    @NotBlank
    private String name;

    private String description;

    private Integer price;

    private Integer stock_quantity;

    private String category;
}
