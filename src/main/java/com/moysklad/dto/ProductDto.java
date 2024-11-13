package com.moysklad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name cannot be longer than 255 characters")
    private String name;

    @Size(max = 4096, message = "Product description cannot be longer than 4096 characters")
    private String description;

    @Min(value = 0, message = "The price of the product cannot be less than 0")
    private double price = 0.0;

    private boolean inStock = false;

    public ProductDto() {
    }

    public ProductDto(Long id, String name, String description, double price, boolean inStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}