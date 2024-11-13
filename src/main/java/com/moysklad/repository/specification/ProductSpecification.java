package com.moysklad.repository.specification;

import com.moysklad.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Возвращает все записи, если фильтр пустой
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasPriceGreaterThanOrEqual(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                minPrice == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> hasPriceLessThanOrEqual(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> isInStock(Boolean inStock) {
        return (root, query, criteriaBuilder) ->
                inStock == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("inStock"), inStock);
    }
}
