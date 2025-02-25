package com.backend.integration;

import com.backend.dtos.EntityToDTOMapper;
import com.backend.dtos.Order.OrderDTO;
import com.backend.entity.Order.OrderDetail;
import com.backend.entity.Order.OrderUser;
import com.backend.profis_service.ProfisOrderService;
import com.backend.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OrderIntegrationTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProfisOrderService profisOrderService;

    @Test
    void createOrderDetailTest() {
        // Arrange
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(8291);
        orderUser.setOrderDetail(orderDetail);
        OrderDTO dto = EntityToDTOMapper.mapToOrderDTO(orderDetail);
        Long userId = 10L;
        // Act
        int result3 = profisOrderService.CreateOrderDetailRequest(userId);
        // Assert
        assertEquals(result3, (dto.getOrderDetail().getId()));
    }
    @Test
    void createOrderListTest() {
        // Arrange
        Long userId = 10L;
        // Act
        String result = profisOrderService.CreateOrderListRequest(userId);
        // Assert
        assertEquals("<Data><ID>8292</ID><Klic>EDC0B696</Klic><ID>8291</ID><Klic>D88A537D</Klic></Data>", result);
    }
    @Test
    void getOrderDetailTest() {
        // Arrange
        int userId = 10;
        // Act
        OrderDTO dto = orderService.getOrderDetail(userId);
        // Assert
        assertEquals(8291, dto.getOrderDetail().getId());
    }



    @Test
    void createOrderListTest_throwsExceptionWhenPasswordNotFound() {
        // Arrange: Use a non-existent ID to trigger the exception scenario
        Long nonExistentId = 9999L; // Assuming this ID does not exist in your database

        // Act & Assert: Verify the exception is thrown
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> profisOrderService.CreateOrderListRequest(nonExistentId),
                "Expected CreateOrderListRequest to throw IllegalStateException when password is not found"
        );

        // Assert: Verify the exception message
        assertTrue(
                exception.getMessage().contains("Encrypted password not found for ID:"),
                "Exception message should contain the expected text"
        );
    }

    @Test
    void createOrderListTest_throwsExceptionWhenIdNotFound() {
        // Arrange: Use a non-existent ID to trigger the exception scenario
        Long nonExistentId = null; // Assuming this ID does not exist in your database

        // Act & Assert: Verify the exception is thrown
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> profisOrderService.CreateOrderListRequest(nonExistentId),
                "Expected CreateOrderListRequest to throw IllegalStateException when password is not found"
        );

        // Assert: Verify the exception message
        assertTrue(
                exception.getMessage().contains("ID cannot be null"),
                "Exception message should contain the expected text"
        );
    }

}
