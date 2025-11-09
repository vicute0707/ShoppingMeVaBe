package iuh.student.www.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import iuh.student.www.dto.RegisterDTO;
import iuh.student.www.entity.User;
import iuh.student.www.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Guest - Authentication", description = "User registration API (Guest access)")
public class AuthRestController {

    private final UserService userService;

    @Operation(summary = "Register new user", description = "Register a new customer account with email notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Validation failed or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            // Register user using the service (it will check for duplicates and send welcome email)
            User savedUser = userService.registerUser(registerDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful! Welcome email has been sent.");
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
