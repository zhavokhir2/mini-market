package uz.mini_market.market.dto;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Long productId;
    private Integer quantity;
}
