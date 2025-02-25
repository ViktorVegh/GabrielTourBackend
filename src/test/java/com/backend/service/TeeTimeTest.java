package com.backend.service;

import com.backend.repository.GolfCourseRepository;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TeeTimeTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GolfCourseRepository golfCourseRepository;
    @InjectMocks
    private TeeTimeService teeTimeService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
/*
    @Test
    void createTeeTime_Zero() {
        TeeTimeRequest teeTimeRequest = new TeeTimeRequest();
        Long[] longArray = {};
        List<Long> userIds = Arrays.asList(longArray);
        teeTimeRequest.setUserIds(userIds);
        // Mock the repository behavior to return null
        when(userRepository.findAllById(teeTimeRequest.getUserIds())).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teeTimeService.createTeeTime(teeTimeRequest));

        // Verify that the correct exception message is thrown
        assertEquals("No valid users found for provided user IDs", exception.getMessage());

        // Verify interactions
        verify(userRepository).findAllById(userIds);

    }
    @Test
    void createTeeTime_One() {
        TeeTimeRequest teeTimeRequest = new TeeTimeRequest();
        Long[] longArray = {1L, 2L};
        List<Long> userIds = Arrays.asList(longArray);
        teeTimeRequest.setUserIds(userIds);
        User user = new User();
        List<User> users = List.of(user);
        // Mock the repository behavior to return null
        when(golfCourseRepository.findById(teeTimeRequest.getGolfCourseId())).thenReturn(Optional.empty());
        when(userRepository.findAllById(teeTimeRequest.getUserIds())).thenReturn(users);
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teeTimeService.createTeeTime(teeTimeRequest));

        // Verify that the correct exception message is thrown
            assertEquals("GolfCourse with ID null not found", exception.getMessage());

        // Verify interactions
        verify(userRepository).findAllById(userIds);

    }
    @Test
    void getTeeTime_One() {

        // Mock the repository behavior to return null
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teeTimeService.getTeeTimesByUserId(1L));

        // Verify that the correct exception message is thrown
        assertEquals("User not found", exception.getMessage());

        // Verify interactions
        verify(userRepository).findById(1L);

    }*/

}
