package com.backend.profis_service;

import com.backend.auth.EncryptionUtil;
import com.backend.entity.Order.OrderUser;
import com.backend.profis_service_interface.ProfisOrderServiceInterface;
import com.backend.repository.OrderUserRepository;
import com.backend.repository.UserRepository;
import com.backend.service.OrderService;
import com.example.klientsoapclient.Klient;
import com.example.klientsoapclient.KlientHesloContext;
import com.example.klientsoapclient.KlientObjednavkaListResult;
import com.example.objednavkasoapclient.Objednavka;
import com.example.objednavkasoapclient.ObjednavkaContext;
import com.example.objednavkasoapclient.ObjednavkaDetailResult;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.namespace.QName;
import java.net.URL;

@org.springframework.stereotype.Service

public class ProfisOrderService implements ProfisOrderServiceInterface {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderUserRepository orderUserRepository;

    private static final String WSDL_URL = "https://xml.gabrieltour.sk/API/v1/Klient.svc?wsdl";

    private static final String WSDL_URL2 = "https://xml.gabrieltour.sk/API/v1/Objednavka.svc?wsdl";

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "KlientService");
    private static final QName SERVICE_NAME2 = new QName("http://tempuri.org/", "ObjednavkaService");
    private static final QName PORT_NAME = new QName("http://tempuri.org/", "BasicHttpsBinding_Klient");
    private static final QName PORT_NAME2 = new QName("http://tempuri.org/", "BasicHttpsBinding_Objednavka");

    private static final String NAMESPACE_URI = "http://xml.profis.profitour.cz";



    private String passwordClient="aplikacia";
    private String passwordUser="aplikacia";
    private String usernameUser="jbabica@2";

    private String id="01";

    JAXBElement<String> passwordElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelHeslo"), String.class, passwordUser);
    JAXBElement<String> usernameElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelLogin"), String.class, usernameUser);
    JAXBElement<String> idElement = new JAXBElement<>(new QName(NAMESPACE_URI, "id_Jazyk"), String.class, id);
    private Klient klientPort;
    private Objednavka objednavkaPort;

    public ProfisOrderService() throws Exception {
        // Initialize the klient service and port
        URL wsdlLocation = new URL(WSDL_URL);
        Service service = Service.create(wsdlLocation, SERVICE_NAME);
        klientPort = service.getPort(PORT_NAME, Klient.class);

        // initialize oreder service and port
        URL wsdlLocation2 = new URL(WSDL_URL2);
        Service service2 = Service.create(wsdlLocation2, SERVICE_NAME2);
        objednavkaPort = service2.getPort(PORT_NAME2, Objednavka.class);
    }

    @Override
    public String CreateOrderListRequest(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        KlientHesloContext context = new KlientHesloContext();

        // Fetch the encrypted password and check for null
        String encryptedPassword = userRepository.getPasswordById(id.intValue());
        if (encryptedPassword == null) {
            throw new IllegalStateException("Encrypted password not found for ID: " + id);
        }

        // Fetch the ProfisId and check for a valid value
        Integer profisId = userRepository.getProfisId(id.intValue());
        if (profisId == null) {
            throw new IllegalStateException("ProfisId not found for ID: " + id);
        }

        // Decrypt the password and handle potential errors
        String plaintextPassword;
        try {
            plaintextPassword = EncryptionUtil.decrypt(encryptedPassword);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt password for ID: " + id, e);
        }

        // Set the plaintext password in the context
        context.setKlientHeslo(new JAXBElement<>(
                new QName("http://xml.profis.profitour.cz", "KlientHeslo"),
                String.class,
                plaintextPassword
        ));
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(false);               // Set to false as per request
        context.setIdJazyk(idElement);
        //will be modified my variable above in try clause
        context.setIdKlient(profisId);
        KlientObjednavkaListResult result = klientPort.klientObjednavkaList(context);
        if (result == null) {
            throw new IllegalArgumentException("No orders found for the given user ID: " + id+" in profis");
        }
        String FinalResult = orderService.createOrderList(result,id);
        return FinalResult;

    }
    @Override
    public int CreateOrderDetailRequest(long id) {
        ObjednavkaContext context = new ObjednavkaContext();
        context.setUzivatelHeslo(passwordElement); // Set the user's password
        context.setUzivatelLogin(usernameElement); // Set the user's login
        context.setVypsatNazvy(true);              // Set to false as per request
        context.setIdJazyk(idElement);

        // Fetch the OrderUser and handle the case where it might be null
        OrderUser orderUser = orderUserRepository.findClosestOrderByUserId(id);
        if (orderUser == null) {
            throw new IllegalArgumentException("No Order found for the given user ID: " + id+" in database");
        }

        int orderId = orderUser.getOrderDetail().getId(); // Extract orderId from the composite key
        String klic = orderUser.getKlic(); // Get the `klic` field

        // Use `orderId` and `klic` as needed
        System.out.println("Order ID: " + orderId);
        System.out.println("Klic: " + klic);
        context.setIdObjednavka(orderId);
        context.setKlic(new JAXBElement<>(new QName(NAMESPACE_URI, "Klic"), String.class, klic));

        ObjednavkaDetailResult result = objednavkaPort.objednavkaDetail(context);
        if (result == null) {
            throw new IllegalArgumentException("OrderUser does not exist in Profis system");
        }
        int FinalResult = orderService.createOrderDetail(result);
        return FinalResult;
    }
}
