package com.backend.profis_service;

import com.example.klientsoapclient.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@org.springframework.stereotype.Service
public class LoginService {

    private static final String WSDL_URL = "https://xml.gabrieltour.sk/API/v1/Klient.svc?wsdl";

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "KlientService");

    private static final QName PORT_NAME = new QName("http://tempuri.org/", "BasicHttpsBinding_Klient");


    private static final String NAMESPACE_URI = "http://xml.profis.profitour.cz";



    private String passwordClient="password";
    private String passwordUser="aplikacia";
    private String usernameUser="jbabica@2";

    private String username="user";

    private String id="01";
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
    public OveritEmailResult overitEmail(String email){
        System.out.println("i got to service");
        Context context = new Context();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(false);               // Set to false as per request
        context.setIdJazyk(idElement);
        OveritEmailResult CallResponse;
        CallResponse=klientPort.overitEmail(context,email);
        System.out.println(CallResponse);
        return CallResponse;
    }
    public ResetHeslaOdeslatResult resetHeslaOdeslat(int clientId){
        Context context = new Context();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(false);               // Set to false as per request
        context.setIdJazyk(idElement);
        ResetHeslaOdeslatInput resetHeslaOdeslatInput= new ResetHeslaOdeslatInput();

        try {
            // Set platnost to 24 hours from now
            GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            gregorianCalendar.add(Calendar.HOUR, 20); // Add 24 hours to the current time

            // Convert GregorianCalendar to XMLGregorianCalendar
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(gregorianCalendar);
            resetHeslaOdeslatInput.setPlatnost(xmlGregorianCalendar);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating XMLGregorianCalendar for platnost.");
        }
        resetHeslaOdeslatInput.setIdPravidlo(31);
        resetHeslaOdeslatInput.setIdKlient(clientId);
        ResetHeslaOdeslatResult CallResponse;
        CallResponse=klientPort.resetHeslaOdeslat(context,resetHeslaOdeslatInput);
        System.out.println(CallResponse);
        return CallResponse;
    }
    public ResetHeslaOveritResult resetHeslaOverit(String authKey,String email,int clientId){
        KlientContextBase klientContextBase = new KlientContextBase();
        klientContextBase.setUzivatelHeslo(passwordElement);
        klientContextBase.setUzivatelLogin(usernameElement);
        klientContextBase.setVypsatNazvy(false);
        klientContextBase.setIdJazyk(idElement);
        klientContextBase.setIdKlient(clientId);

        // Step 2: Set up the key context
        KlientKlicContext klientKlicContext = new KlientKlicContext();
        klientKlicContext.setUzivatelHeslo(klientContextBase.getUzivatelHeslo());
        klientKlicContext.setUzivatelLogin(klientContextBase.getUzivatelLogin());
        klientKlicContext.setVypsatNazvy(klientContextBase.isVypsatNazvy());
        klientKlicContext.setIdJazyk(klientContextBase.getIdJazyk());
        klientKlicContext.setIdKlient(klientContextBase.getIdKlient());

        // Set the email and authKey in the key context using JAXBElement
        klientKlicContext.setEmail(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "Email"), String.class, email));
        klientKlicContext.setKlic(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "Klic"), String.class, authKey));

        // Step 3: Call the API with the complete context
        ResetHeslaOveritResult result = klientPort.resetHeslaOverit(klientKlicContext);
        System.out.println(result);
        return result;
    }
    public ZmenitHesloResult changePassword(String authKey,String password,int clientId,String email){
        KlientContextBase klientContextBase = new KlientContextBase();
        klientContextBase.setUzivatelHeslo(passwordElement);
        klientContextBase.setUzivatelLogin(usernameElement);
        klientContextBase.setVypsatNazvy(false);
        klientContextBase.setIdJazyk(idElement);
        klientContextBase.setIdKlient(clientId);

        KlientKlicContext klientKlicContext = new KlientKlicContext();
        klientKlicContext.setUzivatelHeslo(klientContextBase.getUzivatelHeslo());
        klientKlicContext.setUzivatelLogin(klientContextBase.getUzivatelLogin());
        klientKlicContext.setVypsatNazvy(klientContextBase.isVypsatNazvy());
        klientKlicContext.setIdJazyk(klientContextBase.getIdJazyk());
        klientKlicContext.setIdKlient(klientContextBase.getIdKlient());

        // Set the email and authKey in the key context using JAXBElement
        klientKlicContext.setKlic(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "Klic"), String.class, authKey));
        klientKlicContext.setEmail(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "Email"), String.class, email));
        ZmenitHesloResult result = klientPort.zmenitHeslo(klientKlicContext,password);
        return result;
    }
}
