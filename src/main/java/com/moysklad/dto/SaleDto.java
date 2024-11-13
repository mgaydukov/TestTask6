package com.moysklad.dto;

import jakarta.validation.constraints.*;

public class SaleDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String documentName;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double price;

    public SaleDto() {
    }

    public SaleDto(String documentName, Long productId, Integer quantity, Double price) {
        this.documentName = documentName;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public SaleDto(Long id, String documentName, Long productId, Integer quantity, Double price) {
        this.id = id;
        this.documentName = documentName;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

