package uz.mini_market.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mini_market.market.dto.PromoteRequest;
import uz.mini_market.market.service.UserService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping()
    public ResponseEntity<String> promoteUserToAdmin(@RequestBody PromoteRequest request) {
        userService.promoteToAdminByUsername(request.getUsername());
        return ResponseEntity.ok("User promoted to ADMIN successfully");
    }

}
