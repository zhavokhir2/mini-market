package uz.mini_market.market.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.mini_market.market.dto.AuthRequest;
import uz.mini_market.market.dto.AuthResponse;
import uz.mini_market.market.dto.RegisterRequest;
import uz.mini_market.market.model.User;
import uz.mini_market.market.model.enums.Role;
import uz.mini_market.market.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private static final long CODE_EXPIRATION_MINUTES = 10;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        String confirmationCode = generateRandomCode();

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .email(request.getEmail())
                .confirmationCode(confirmationCode)
                .codeGeneratedAt(LocalDateTime.now())
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendConfirmationCode(savedUser.getEmail(), confirmationCode);

        return savedUser;
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Проверяем логин/пароль через Spring Security
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        authenticationManager.authenticate(authentication);

        // Генерируем JWT токен
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }



    public boolean verifyEmail(String username, String code) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        if (!user.getConfirmationCode().equals(code)) {
            throw new RuntimeException("Invalid confirmation code");
        }

        LocalDateTime generatedAt = user.getCodeGeneratedAt();
        if (generatedAt == null || generatedAt.plusMinutes(CODE_EXPIRATION_MINUTES).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Confirmation code expired");
        }

        user.setEmailVerified(true);
        user.setConfirmationCode(null);
        user.setCodeGeneratedAt(null);
        userRepository.save(user);
        return true;
    }

    public void resendConfirmationCode(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        // Генерируем новый код и обновляем время
        String newCode = generateRandomCode();
        user.setConfirmationCode(newCode);
        user.setCodeGeneratedAt(LocalDateTime.now());

        userRepository.save(user);

        emailService.sendConfirmationCode(user.getEmail(), newCode);
    }
    private String generateRandomCode() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }

    public void promoteToAdminByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return user.getId();
    }


}

