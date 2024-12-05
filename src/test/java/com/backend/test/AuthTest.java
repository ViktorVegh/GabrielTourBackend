package com.backend.test;

import com.backend.auth.AuthService;
import com.backend.auth.JwtUtil;
import com.backend.entity.*;
import com.backend.repository.DriverRepository;
import com.backend.repository.OfficeRepository;
import com.backend.repository.TourGuideRepository;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private TourGuideRepository tourGuideRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }



    @Test
    void testRegisterUser_EmailAlreadyRegistered() {
        String email = "testuser@example.com";
        String password = "password";

        // Mock the behavior of repositories to simulate email already registered
        when(userRepository.findByEmail(email)).thenReturn(new User());

        // Check that the exception is thrown and the message is correct
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.registerEmployee(email, password, null, null, "user"));
        assertEquals("Email is already registered!", exception.getMessage());
    }

    @Test
    void testLogin_InvalidCredentials() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, password));
        assertEquals("Invalid login credentials!", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        String email = "testuser@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        User user = new User(email, encodedPassword, "User Name", "profile.jpg", "user");
        user.setId(1L);
        // Mock repository and other dependencies
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // Mock generateToken to return a test token
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("userToken");

        // Execute login
        String token = authService.login(email, password);
        System.out.println("Token returned by login: " + token);
        // Verify the token is as expected
        assertEquals("userToken", token);
    }


    @Test
    void testRegisterUser_Success() {
        String email = "testuser@example.com";
        String password = "password";
        String name = "Test User";
        String profilePicture = "testpic.jpg";
        String role = "user";
        Long id = 1L;

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(id); // set the id manually since the real save isn't happening
            return user;
        });
        when(jwtUtil.generateToken(anyLong(), eq(role))).thenReturn("testToken");

        String result = authService.registerEmployee(email, password, name, profilePicture, role);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("testToken", result);
    }
}