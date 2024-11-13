package com.moysklad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SupplyDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String documentName;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public SupplyDto() {
    }

    public SupplyDto(String documentName, Long productId, Integer quantity) {
        this.documentName = documentName;
        this.productId = productId;
        this.quantity = quantity;
    }

    public SupplyDto(Long id, String documentName, Long productId, Integer quantity) {
        this.id = id;
        this.documentName = documentName;
        this.productId = productId;
        this.quantity = quantity;
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
}
