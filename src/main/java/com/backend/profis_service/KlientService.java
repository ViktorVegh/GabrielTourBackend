package com.backend.profis_service;


import com.backend.profis_service_interface.KlientServiceInterface;
import com.example.klientsoapclient.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.net.URL;

@org.springframework.stereotype.Service
public class KlientService implements KlientServiceInterface {
    private static final String WSDL_URL = "https://xml.gabrieltour.sk/API/v1/Klient.svc?wsdl";

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "KlientService");

    private static final QName PORT_NAME = new QName("http://tempuri.org/", "BasicHttpsBinding_Klient");


    private static final String NAMESPACE_URI = "http://xml.profis.profitour.cz";

    private String passwordUser="aplikacia";
    private String usernameUser="jbabica@2";
    private XMLGregorianCalendar narozeniDate;
    private XMLGregorianCalendar vydaniDokladuDate;
    private XMLGregorianCalendar platnostDokladuDate;
    private AdresaInputBase adresaValue;

    KlientDataInput klientDataInput = new KlientDataInput();


    @Override
    public void initialize()throws Exception{
        narozeniDate = DatatypeFactory.newInstance().newXMLGregorianCalendar("1980-01-01");
        vydaniDokladuDate = DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-01-01");
        platnostDokladuDate = DatatypeFactory.newInstance().newXMLGregorianCalendar("2030-01-01");
        adresaValue = new AdresaInputBase();
        String dummyStreet = "Dummy Street";
        String dummyCP = "1234"; // Example code or house number
        adresaValue.setUlice(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "Ulice"), String.class, dummyStreet));
        adresaValue.setCP(new JAXBElement<>(new QName("http://xml.profis.profitour.cz", "CP"), String.class, dummyCP));
    }

    // Create JAXBElement values for each field and set them
    @Override
    public KlientDataInput setKlientDataInput() throws Exception {
        initialize();
        klientDataInput.setAdresa(new JAXBElement<>(new QName(NAMESPACE_URI, "Adresa"), AdresaInputBase.class, adresaValue)); // Assuming adresaValue is of type AdresaInputBase
        klientDataInput.setCisloDokladu(new JAXBElement<>(new QName(NAMESPACE_URI, "CisloDokladu"), String.class, "123456789"));
        klientDataInput.setEmail(new JAXBElement<>(new QName(NAMESPACE_URI, "Email"), String.class, "client@example.com"));
        klientDataInput.setJmeno(new JAXBElement<>(new QName(NAMESPACE_URI, "Jmeno"), String.class, "John"));
        klientDataInput.setNarozeni(new JAXBElement<>(new QName(NAMESPACE_URI, "Narozeni"), XMLGregorianCalendar.class, narozeniDate)); // XMLGregorianCalendar instance
        klientDataInput.setPlatnostDokladu(new JAXBElement<>(new QName(NAMESPACE_URI, "PlatnostDokladu"), XMLGregorianCalendar.class, platnostDokladuDate)); // XMLGregorianCalendar instance
        klientDataInput.setPrijmeni(new JAXBElement<>(new QName(NAMESPACE_URI, "Prijmeni"), String.class, "Doe"));
        klientDataInput.setTelefon(new JAXBElement<>(new QName(NAMESPACE_URI, "Telefon"), String.class, "1234567890"));
        klientDataInput.setTitul(new JAXBElement<>(new QName(NAMESPACE_URI, "Titul"), String.class, "Mr."));
        klientDataInput.setVydaniDokladu(new JAXBElement<>(new QName(NAMESPACE_URI, "VydaniDokladu"), XMLGregorianCalendar.class, vydaniDokladuDate)); // XMLGregorianCalendar instance
        klientDataInput.setVystavitelDokladu(new JAXBElement<>(new QName(NAMESPACE_URI, "VystavitelDokladu"), String.class, "Government Authority"));
        klientDataInput.setIdPohlavi(new JAXBElement<>(new QName(NAMESPACE_URI, "id_Pohlavi"), String.class, "M"));
        klientDataInput.setIdStatniPrislusnost(new JAXBElement<>(new QName(NAMESPACE_URI, "id_StatniPrislusnost"), Integer.class, 1)); // Country ID
        klientDataInput.setIdTypCestovniDoklad(new JAXBElement<>(new QName(NAMESPACE_URI, "id_TypCestovniDoklad"), Integer.class, 2)); // Document type ID
        return klientDataInput;
    }


    JAXBElement<String> passwordElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelHeslo"), String.class, passwordUser);

    JAXBElement<String> usernameElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelLogin"), String.class, usernameUser);
    JAXBElement<KlientDataInput> klientDataInputJAXBElement= new JAXBElement<>(new QName(NAMESPACE_URI,"klientData"), KlientDataInput.class,klientDataInput);
    private Klient klientPort;

    public KlientService() throws Exception {
        // Initialize the service and port
        URL wsdlLocation = new URL(WSDL_URL);
        Service service = Service.create(wsdlLocation, SERVICE_NAME);
        klientPort = service.getPort(PORT_NAME, Klient.class);
    }
    @Override
    public RegistrovatResult Registrovat(){
        Context context = new Context();
        try {
            setKlientDataInput();
            RegistrovatKlientInput registrovatKlientInput = new RegistrovatKlientInput();
            //RegistrovatInputBase registrovatInputBase = new RegistrovatInputBase();
            registrovatKlientInput.setKlient(klientDataInputJAXBElement);
            context.setUzivatelHeslo(passwordElement);    // Set the user's password
            context.setUzivatelLogin(usernameElement);    // Set the user's login
            context.setVypsatNazvy(false);               // Set to false as per request
            RegistrovatResult CallResponse;
            CallResponse= klientPort.registrovat(context,registrovatKlientInput);
            return CallResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Error handling can be improved as needed
        }

    }

}
