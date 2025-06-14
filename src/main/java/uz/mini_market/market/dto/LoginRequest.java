package uz.mini_market.market.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
