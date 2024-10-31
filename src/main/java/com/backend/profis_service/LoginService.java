package com.backend.profis_service;

import com.example.klientsoapclient.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.namespace.QName;
import java.net.URL;
@org.springframework.stereotype.Service
public class LoginService {

    private static final String WSDL_URL = "https://xml.gabrieltour.sk/API/v1/Klient.svc?wsdl";

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "KlientService");

    private static final QName PORT_NAME = new QName("http://tempuri.org/", "BasicHttpsBinding_Klient");


    private static final String NAMESPACE_URI = "http://xml.profis.profitour.cz";

    private String email="test";

    private String passwordClient="password";
    private String passwordUser="aplikacia";
    private String usernameUser="jbabica@2";

    private String username="user";

    private String id="01";

    JAXBElement<String> emailClientElement = new JAXBElement<>(new QName(NAMESPACE_URI, "Email"), String.class,email);
    JAXBElement<String> passwordElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelHeslo"), String.class, passwordUser);
    JAXBElement<String> passwordClientElement = new JAXBElement<>(new QName(NAMESPACE_URI, "Heslo"), String.class, passwordClient);
    JAXBElement<String> usernameElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelLogin"), String.class, usernameUser);
    JAXBElement<String> idElement = new JAXBElement<>(new QName(NAMESPACE_URI, "id_Jazyk"), String.class, id);
    private Klient klientPort;


    public LoginService() throws Exception {
        // Initialize the service and port
        URL wsdlLocation = new URL(WSDL_URL);
        Service service = Service.create(wsdlLocation, SERVICE_NAME);
        klientPort = service.getPort(PORT_NAME, Klient.class);
    }

    public KlientPrihlasitResult login(String email, String password) {

        // Set up context if required
        Context context = new Context();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(false);               // Set to false as per request
        context.setIdJazyk(idElement);
        KlientPrihlasitResult CallResponse;
        System.out.println("okk");
        // Invoke the SOAP service method
        CallResponse=klientPort.klientPrihlasit(context,email,password);
        System.out.println(CallResponse);
        return CallResponse;
    }
}
