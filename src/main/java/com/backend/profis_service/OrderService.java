package com.backend.profis_service;

import com.backend.auth.EncryptionUtil;
import com.backend.entity.User;
import com.backend.repository.UserRepository;
import com.example.klientsoapclient.Context;
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
import java.util.Optional;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;


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
    JAXBElement<String> passwordClientElement = new JAXBElement<>(new QName(NAMESPACE_URI, "Heslo"), String.class, passwordClient);
    JAXBElement<String> usernameElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelLogin"), String.class, usernameUser);
    JAXBElement<String> idElement = new JAXBElement<>(new QName(NAMESPACE_URI, "id_Jazyk"), String.class, id);
    private Klient klientPort;
    private Objednavka objednavkaPort;


    public OrderService() throws Exception {
        // Initialize the klient service and port
        URL wsdlLocation = new URL(WSDL_URL);
        Service service = Service.create(wsdlLocation, SERVICE_NAME);
        klientPort = service.getPort(PORT_NAME, Klient.class);

        // initialize oreder service and port
        URL wsdlLocation2 = new URL(WSDL_URL2);
        Service service2 = Service.create(wsdlLocation2, SERVICE_NAME2);
        objednavkaPort = service2.getPort(PORT_NAME2, Objednavka.class);
    }

    public KlientObjednavkaListResult klientObjednavkaList(int id){
        KlientHesloContext context = new KlientHesloContext();
        String encryptedPassword = userRepository.getPasswordById(id);
        System.out.println("HESLO"+encryptedPassword);
        //int id = userRepository.getProfisId();
        // Decrypt the password
        String plaintextPassword = EncryptionUtil.decrypt(encryptedPassword);

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
        context.setIdKlient(9388);
        KlientObjednavkaListResult result = klientPort.klientObjednavkaList(context);
        return result;


    }
    public ObjednavkaDetailResult ObjednavkaDetail(int id){
        ObjednavkaContext context = new ObjednavkaContext();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(true);               // Set to false as per request
        context.setIdJazyk(idElement);
        context.setIdObjednavka(8291);
        String klic = "D88A537D";
        context.setKlic(new JAXBElement<>(new QName(NAMESPACE_URI, "Klic"), String.class, klic));
        ObjednavkaDetailResult result = objednavkaPort.objednavkaDetail(context);
        return result;


    }

}
