package com.backend.controller;

import com.backend.auth.AuthService;
import com.backend.auth.AuthServiceInterface;
import com.backend.dtos.LoginRequest;
import com.backend.dtos.RegisterRequest;
import com.backend.profis_service.LoginService;
import com.backend.profis_service_interface.LoginServiceInterface;
import com.example.klientsoapclient.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceInterface authService;
    @Autowired
    private LoginServiceInterface loginService;
    // Register endpoint
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest registerRequest) {
        String token = authService.registerEmployee(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getName(),
                registerRequest.getProfilePicture(),
                registerRequest.getRole()
        );


        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // Login endpoint
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());


        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    } //soap login
    @PostMapping("/login-klient")
    public KlientPrihlasitResult loginClient(@RequestBody LoginRequest loginRequest) {
        try {
            URL url = new URL("https://checkip.amazonaws.com"); // AWS service to get the public IP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ipAddress = in.readLine();
            in.close();

            System.out.println("Public IP Address of this AWS instance: " + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        KlientPrihlasitResult result = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        System.out.println(result);
        return result;
    }

    // Logout endpoint
    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "Logged out successfully";
    }
    // Endpoint to send the password reset email
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        try {
            // Step 1: Call OveritEmail to verify email existence and retrieve id_Klient
            OveritEmailResult result = loginService.overitEmail(email);
            if (result == null || result.getData() == null || result.getData().getValue() == null) {
                response.put("message", "Email not found.");
                return response;
            }

            // Ensure the list contains exactly one ID before accessing it
            List<Integer> clientIds = result.getData().getValue().getInt();
            if (clientIds == null || clientIds.isEmpty()) {
                response.put("message", "Email not associated with any client.");
                return response;
            } else if (clientIds.size() > 1) {
                throw new IllegalArgumentException("Multiple accounts found for the provided email.");
            }

            // Access the single client ID safely
            Integer clientId = clientIds.get(0);

            // Step 2: Call ResetHeslaOdeslat to send the reset password email
            ResetHeslaOdeslatResult emailSent = loginService.resetHeslaOdeslat(clientId);

            if (emailSent != null) {
                response.put("message", "Password reset link has been sent to your email.");
            } else {
                response.put("message", "Error sending password reset link.");
            }
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling ProfiTour API.", e);
        }
    }
    // Endpoint to verify the authorization key
    @CrossOrigin(origins = "*")
    @GetMapping("/verify-key")
    public ResponseEntity<Map<String, String>> verifyKey(
            @RequestParam("authKey") String authKey,
            @RequestParam("email") String email,
            @RequestParam("clientId") int clientId) {

        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("Received request to verify key with authKey: " + authKey + ", email: " + email + ", clientId: " + clientId);

            // Call the service method to verify the authorization key
            ResetHeslaOveritResult result = loginService.resetHeslaOverit(authKey, email, clientId);

            // Check if the response from the service indicates a valid key
            if (result.getMessage() != null && result.getMessage().getValue() != null) {
                response.put("status", "error");
                System.out.println("Debug: result.getMessage() = " + result.getMessage());
                response.put("message", "Authorization key is invalid or has expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

            } else {
                response.put("status", "success");
                response.put("message", "Authorization key is valid.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();

            // Return a response with a 500 status code in case of server error
            response.put("status", "error");
            response.put("message", "Error verifying authorization key.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Endpoint to change the password
    @PostMapping("/change-password")
    public Map<String, String> changePassword(@RequestBody Map<String, String> request) {
        String authKey = request.get("authKey");
        String newPassword = request.get("newPassword");
        String email =request.get("email");
        int profisId;
        Map<String, String> response = new HashMap<>();

        try {
            profisId = Integer.parseInt(request.get("clientId"));
            System.out.println(profisId+"KUrvaaaaaaaaaa");
            ZmenitHesloResult result = loginService.changePassword(authKey, newPassword,profisId,email);

            if (result.getMessage().getValue() == null) {
                response.put("message", "Password has been successfully changed.");
                try {
                    authService.registerUser(email,newPassword, (long) profisId);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                response.put("message", "Error changing password.");
            }
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error changing password.");
        }
    }
}
