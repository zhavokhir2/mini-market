package uz.mini_market.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import uz.mini_market.market.dto.AuthRequest;
import uz.mini_market.market.dto.AuthResponse;
import uz.mini_market.market.dto.RegisterRequest;
import uz.mini_market.market.dto.VerifyEmailRequest;
import uz.mini_market.market.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticate(request);
        return ResponseEntity.ok(response);
}


    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            boolean verified = userService.verifyEmail(request.getUsername(), request.getCode());

            if (verified) {
                return ResponseEntity.ok("Email verified successfully");
            } else {
                return ResponseEntity.badRequest().body("Email verification failed");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        try {
            userService.resendConfirmationCode(username);
            return ResponseEntity.ok("Confirmation code resent successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
