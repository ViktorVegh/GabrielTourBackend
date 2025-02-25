package com.backend.service;

import com.backend.auth.EncryptionUtil;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.dtos.Order.OrderDTO;
import com.backend.entity.Order.OrderDetail;
import com.backend.entity.Order.OrderUser;
import com.backend.profis_service.ProfisOrderService;
import com.backend.repository.OrderUserRepository;
import com.backend.repository.UserRepository;
import com.example.klientsoapclient.KlientHesloContext;
import com.example.objednavkasoapclient.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.klientsoapclient.Klient;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderTest {
    @Mock
    private OrderUserRepository orderUserRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @InjectMocks
    private ProfisOrderService ProfisOrderService;

    @Mock
    private Objednavka objednavkaPort;
    @Mock
    private Klient klientPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getOrderDetail_Zero() {

        // Mock the repository behavior to return null
        when(orderUserRepository.findClosestOrderByUserId(10L)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrderDetail(10));

        // Verify that the correct exception message is thrown
        assertEquals("No order details found for user ID: 10", exception.getMessage());

        // Verify interactions
        verify(orderUserRepository).findClosestOrderByUserId(10L);
        verifyNoMoreInteractions(orderUserRepository);
    }
    @Test
    void getOrderDetail_One() {
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(9833); // Example ID
        orderUser.setOrderDetail(orderDetail);

        OrderDTO dto = EntityToDTOMapper.mapToOrderDTO(orderDetail);

        when(orderUserRepository.findClosestOrderByUserId(10L)).thenReturn(orderUser);

        OrderDTO result = orderService.getOrderDetail(10);
        assertEquals(result.getOrderDetail().getId(),dto.getOrderDetail().getId());

        // Verify interactions
        verify(orderUserRepository).findClosestOrderByUserId(10L);
        verifyNoMoreInteractions(orderUserRepository);
    }

    @Test
    void createOrderDetail_noOrderFoundForId(){
        // Arrange
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1); // Example ID;
        orderUser.setOrderDetail(orderDetail);

        when(orderUserRepository.findClosestOrderByUserId(1L)).thenReturn(null);
        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderDetailRequest(1));
        // Assert
        assertEquals("No Order found for the given user ID: 1 in database", exception.getMessage());
    }
    @Test
    void createOrderDetail_OrderUserDoesNotExist(){
        // Arrange
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1); // Example ID;
        orderUser.setOrderDetail(orderDetail);
        ObjednavkaContext context  = new ObjednavkaContext();


        when(objednavkaPort.objednavkaDetail(context)).thenReturn(null);
        when(orderUserRepository.findClosestOrderByUserId(1L)).thenReturn(orderUser);
        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderDetailRequest(1));
        // Assert
        assertEquals("OrderUser does not exist in Profis system", exception.getMessage());
    }
    @Test
    void CreateOrderListRequestException0() {

        // Stub methods
        when(userRepository.getPasswordById(1)).thenReturn(null); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(1234);

        // Act & Assert: Validate behavior and exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderListRequest(null)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("ID cannot be null", exception.getMessage());

    }
    @Test
    void CreateOrderListRequestException1() {

        // Stub methods
        when(userRepository.getPasswordById(1)).thenReturn(null); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(1234);

        // Act & Assert: Validate behavior and exception
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> ProfisOrderService.CreateOrderListRequest(1L)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("Encrypted password not found for ID: 1", exception.getMessage());

        // Verify that the expected interactions occurred
        verify(userRepository).getPasswordById(anyInt());
    }
    @Test
    void CreateOrderListRequestException2() {

        // Valid encrypted password (ensure it works with the decryption logic)
        String validEncryptedPassword = EncryptionUtil.encrypt("validPassword");
        // Stub methods
        when(userRepository.getPasswordById(1)).thenReturn(validEncryptedPassword); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(null);

        // Act & Assert: Validate behavior and exception
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> ProfisOrderService.CreateOrderListRequest(1L)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("ProfisId not found for ID: 1", exception.getMessage());

        // Verify that the expected interactions occurred
        verify(userRepository).getPasswordById(anyInt());
        verify(userRepository).getProfisId(anyInt());
    }

    @Test
    void CreateOrderListRequestException3() {

        // Valid encrypted password (ensure it works with the decryption logic)
        String validEncryptedPassword = Base64.getEncoder().encodeToString("validPassword".getBytes());
        // Stub methods
        when(userRepository.getPasswordById(1)).thenReturn(validEncryptedPassword); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(1234);

        // Act & Assert: Validate behavior and exception
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> ProfisOrderService.CreateOrderListRequest(1L)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("Failed to decrypt password for ID: 1", exception.getMessage());

        // Verify that the expected interactions occurred
        verify(userRepository).getPasswordById(anyInt());
        verify(userRepository).getProfisId(anyInt());
    }
    @Test
    void CreateOrderListRequestException4() {

        // Valid encrypted password (ensure it works with the decryption logic)
        String validEncryptedPassword = EncryptionUtil.encrypt("validPassword");
        KlientHesloContext context = new KlientHesloContext();
        // Stub methods
        when(userRepository.getPasswordById(1)).thenReturn(validEncryptedPassword); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(1234);
        when(klientPort.klientObjednavkaList(context)).thenReturn(null);
        // Act & Assert: Validate behavior and exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderListRequest(1L)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("No orders found for the given user ID: 1 in profis", exception.getMessage());

        // Verify that the expected interactions occurred
        verify(userRepository).getPasswordById(anyInt());
        verify(userRepository).getProfisId(anyInt());
    }

    @Test
    void getOrderDetailException1() {

        when(orderUserRepository.findClosestOrderByUserId(1L)).thenReturn(null); // Provide valid encrypted text


        // Act & Assert: Validate behavior and exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrderDetail(1)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("No order details found for user ID: 1", exception.getMessage());

    }


}
