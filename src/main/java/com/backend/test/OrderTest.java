package com.backend.test;

import com.backend.auth.AuthService;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.dtos.OrderDTO;
import com.backend.entity.OrderDetail;
import com.backend.entity.OrderUser;
import com.backend.entity.User;
import com.backend.profis_service.ProfisOrderService;
import com.backend.repository.OrderDetailRepository;
import com.backend.repository.OrderUserRepository;
import com.backend.repository.UserRepository;
import com.backend.service.OrderService;
import com.example.klientsoapclient.KlientHesloContext;
import com.example.klientsoapclient.KlientObjednavkaListResult;
import com.example.objednavkasoapclient.*;
import jakarta.xml.bind.JAXBElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.klientsoapclient.Klient;
import javax.xml.namespace.QName;

import java.lang.reflect.Array;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderTest {
    @Mock
    private OrderUserRepository orderUserRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @InjectMocks
    private ProfisOrderService ProfisOrderService;

    @Mock
    private Objednavka objednavkaPort;

    @Mock
    private com.example.klientsoapclient.Klient klientPort;


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
    void createOrderDetailException1(){
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1); // Example ID;
        orderUser.setOrderDetail(orderDetail);
        //String result = ProfisOrderService.CreateOrderDetailRequest(1);

        when(orderUserRepository.findClosestOrderByUserId(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderDetailRequest(1));

        assertEquals("No OrderUser found for the given user ID: 1 in database", exception.getMessage());
    }
    @Test
    void createOrderDetailException2(){
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1); // Example ID;
        orderUser.setOrderDetail(orderDetail);
        ObjednavkaContext context  = new ObjednavkaContext();


        when(objednavkaPort.objednavkaDetail(context)).thenReturn(null);
        when(orderUserRepository.findClosestOrderByUserId(1L)).thenReturn(orderUser);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderDetailRequest(1));

        assertEquals("OrderUser does not exist in Profis system", exception.getMessage());
    }

    @Test
    void CreateOrderListRequestException1() {
        // Mock dependencies
        ObjednavkaContext context = mock(ObjednavkaContext.class);
        KlientObjednavkaListResult klientObjednavkaListResult = mock(KlientObjednavkaListResult.class);
        OrderService orderService = mock(OrderService.class); // Ensure this is mocked
        UserRepository userRepository = mock(UserRepository.class);
        Klient klientPort = mock(Klient.class);

        // Valid encrypted password (ensure it works with the decryption logic)
        String validEncryptedPassword = Base64.getEncoder().encodeToString("validPassword".getBytes());

        // Stub methods
        when(userRepository.getPasswordById(anyInt())).thenReturn(validEncryptedPassword); // Provide valid encrypted text
        when(userRepository.getProfisId(anyInt())).thenReturn(1234);
        when(klientPort.klientObjednavkaList(any())).thenReturn(null); // Simulate no orders
        when(orderService.createOrderList(any(KlientObjednavkaListResult.class), anyLong())).thenReturn("1");
        System.out.println("Password: " + userRepository.getPasswordById(1));

        // Act & Assert: Validate behavior and exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProfisOrderService.CreateOrderListRequest(1L)); // Ensure ProfisOrderService is instantiated correctly

        // Assert that the exception message matches the expected behavior
        assertEquals("No orders found for the given user ID: 1 in profis", exception.getMessage());

        // Verify that the expected interactions occurred
        verify(userRepository).getPasswordById(anyInt());
        verify(userRepository).getProfisId(anyInt());
        verify(klientPort).klientObjednavkaList(any());
    }


    /*
    @Test
    void createOrderDetail() {
        OrderUser orderUser = new OrderUser();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1); // Example ID;
        orderUser.setOrderDetail(orderDetail);
        ObjednavkaContext context  = new ObjednavkaContext();

        when(orderDetailRepository.getReferenceById(1)).thenReturn(orderDetail);

        ObjednavkaPopis dataValue = new ObjednavkaPopis();
        dataValue.setID(1);
        dataValue.setDospelych(0);
        JAXBElement<ObjednavkaPopis> data = new JAXBElement<>(
                new QName("namespace", "localPart"), // Replace with your namespace and element name
                ObjednavkaPopis.class,
                dataValue
        );

        ObjednavkaDetailResult result= new ObjednavkaDetailResult();
        result.setData(data);
        when(objednavkaPort.objednavkaDetail(context)).thenReturn(result);
        ObjednavkaDetailResult detailResult = objednavkaPort.objednavkaDetail(context);

        System.out.println(detailResult.getData().getValue().getID()+" id");
        System.out.println("test");
        when(orderService.createOrderDetail(detailResult)).thenReturn("1");

        String finalResult = orderService.createOrderDetail(detailResult);

        OrderDTO dto = EntityToDTOMapper.mapToOrderDTO(orderDetail);
        assertEquals(finalResult,dto.getOrderDetail().getId());

        // Verify interactions
        verify(orderUserRepository).findClosestOrderByUserId(10L);
        verifyNoMoreInteractions(orderUserRepository);
    }
     */

}
