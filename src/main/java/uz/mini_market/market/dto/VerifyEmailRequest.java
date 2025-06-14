package uz.mini_market.market.dto;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String username;
    private String code;

}

