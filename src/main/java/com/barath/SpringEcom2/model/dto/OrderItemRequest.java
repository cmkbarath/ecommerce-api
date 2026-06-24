package com.barath.SpringEcom2.model.dto;


public record OrderItemRequest(
        int productId,
        int quantity
) {
}
