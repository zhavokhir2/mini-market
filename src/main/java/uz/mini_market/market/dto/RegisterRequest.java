package uz.mini_market.market.dto;

import lombok.Data;
import uz.mini_market.market.model.enums.Role;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
}

