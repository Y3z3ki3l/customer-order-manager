package com.example.ecommerce.dto;


import lombok.Getter;

@Getter
public class OrderDTO {

    private Long id;
    private String product;
    private Integer quantity;
    private String name;

    public void setId(Long id) {
    this.id = id;
}

    public void setProduct(String product) {
    this.product = product;
}

    public void setQuantity(Integer quantity) {
    this.quantity = quantity;
}


    public void setName(String name) {
        this.name = name;
    }
}