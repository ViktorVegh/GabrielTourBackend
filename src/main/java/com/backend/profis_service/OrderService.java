package com.backend.profis_service;

import com.backend.auth.EncryptionUtil;
import com.backend.entity.OrderUser;
import com.backend.entity.TourOrder;
import com.backend.entity.User;
import com.backend.repository.TourOrderRepository;
import com.backend.repository.UserRepository;
import com.example.klientsoapclient.*;
import com.example.klientsoapclient.Klient;
import com.example.klientsoapclient.KlientKontakt;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.objednavkasoapclient.*;
import com.example.objednavkasoapclient.IntegerNazev;
import com.example.objednavkasoapclient.Objednavka;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.namespace.QName;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TourOrderRepository tourOrderRepository;



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

    public String klientObjednavkaList(Long id){
        KlientHesloContext context = new KlientHesloContext();
        String encryptedPassword = userRepository.getPasswordById(id.intValue());
        System.out.println("HESLO"+encryptedPassword);
        int ProfisId = userRepository.getProfisId(id.intValue());
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
        context.setIdKlient(ProfisId);
        KlientObjednavkaListResult result = klientPort.klientObjednavkaList(context);
        String FinalResult = buildXmlResponseOrderList(result,id);
        return FinalResult;


    }
    public ObjednavkaDetailResult ObjednavkaDetail(int id,String klic){
        ObjednavkaContext context = new ObjednavkaContext();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(true);               // Set to false as per request
        context.setIdJazyk(idElement);
        context.setIdObjednavka(id);
        context.setKlic(new JAXBElement<>(new QName(NAMESPACE_URI, "Klic"), String.class, klic));
        ObjednavkaDetailResult result = objednavkaPort.objednavkaDetail(context);
        return result;
    }
    private String buildXmlResponseOrderList(KlientObjednavkaListResult result,Long id) {
        StringBuilder xml = new StringBuilder();
        xml.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        xml.append("<s:Body>");
        xml.append("<KlientObjednavkaListResponse xmlns=\"http://xml.profis.profitour.cz\">");
        xml.append("<KlientObjednavkaListResult>");

        if (result != null && result.getData() != null && result.getData().getValue() != null) {
            xml.append("<Data>");
            for (ObjednavkaKlient order : result.getData().getValue().getObjednavkaKlient()) {
                xml.append("<ObjednavkaKlient>");
                xml.append("<ID>").append(order.getID()).append("</ID>");
                xml.append("<Klic>").append(getJAXBElementValue(order.getKlic())).append("</Klic>");

                User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));

                // Create a new TourOrder
                TourOrder tourOrder = new TourOrder();
                tourOrder.setOrderNumber(order.getID()); // Example order number
                tourOrder.setOrderDate(LocalDateTime.now());
                String klicValue = getJAXBElementValue(order.getKlic());
                // Create a new OrderUser
                OrderUser orderUser = new OrderUser(tourOrder, user, klicValue);
                tourOrder.setOrderUsers(List.of(orderUser));

                // Save the TourOrder (cascades to OrderUser)
                tourOrderRepository.save(tourOrder);
                xml.append("</ObjednavkaKlient>");
            }
            xml.append("</Data>");
        }

        xml.append("</KlientObjednavkaListResult>");
        xml.append("</KlientObjednavkaListResponse>");
        xml.append("</s:Body>");
        xml.append("</s:Envelope>");

        return xml.toString();
    }

    public String buildXmlResponseOrderDetail(ObjednavkaDetailResult result) {
        StringBuilder xml = new StringBuilder();
        xml.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        xml.append("<s:Body>");
        xml.append("<ObjednavkaDetailResponse xmlns=\"http://xml.profis.profitour.cz\">");
        xml.append("<ObjednavkaDetailResult>");

        if (result != null && result.getData() != null && result.getData().getValue() != null) {
            xml.append("<Data>");
            xml.append("<ObjednavkaDetail>");

            ObjednavkaPopis data = result.getData().getValue();

            // Basic fields
            xml.append("<ID>").append(data.getID()).append("</ID>");
            xml.append("<CenaCelkem>").append(data.getCenaCelkem()).append("</CenaCelkem>");
            xml.append("<DatumOd>").append(data.getDatumOd()).append("</DatumOd>");
            xml.append("<DatumDo>").append(data.getDatumDo()).append("</DatumDo>");
            xml.append("<Dospelych>").append(data.getDospelych()).append("</Dospelych>");
            xml.append("<Deti>").append(data.getDeti()).append("</Deti>");
            xml.append("<Infantu>").append(data.getInfantu()).append("</Infantu>");
            xml.append("<Noci>").append(data.getNoci()).append("</Noci>");
            xml.append("<Klic>").append(data.getKlic()).append("</Klic>");
            xml.append("<Nazev>").append(getJAXBElementValue(data.getNazev())).append("</Nazev>");

            // Letiste
            if (data.getLetiste() != null) {
                xml.append("<Letiste>");
                xml.append("<Nazev>").append(getJAXBElementValue(data.getLetiste())).append("</Nazev>");
                xml.append("</Letiste>");
            }

            // StavObjednavka
            if (data.getStavObjednavka() != null) {
                xml.append("<StavObjednavka>");
                xml.append("<Nazev>").append(data.getStavObjednavka().getName()).append("</Nazev>");
                xml.append("<Nazev>").append(data.getStavObjednavka().getValue()).append("</Nazev>");
                xml.append("</StavObjednavka>");
            }

            // StavPlatba
            if (data.getStavPlatba() != null) {
                xml.append("<StavPlatba>");
                xml.append("<Nazev>").append(data.getStavPlatba().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getStavPlatba().getValue()).append("</ID>");
                xml.append("</StavPlatba>");
            }

            // StavRezervace
            if (data.getStavRezervace() != null) {
                xml.append("<StavRezervace>");
                xml.append("<Nazev>").append(data.getStavRezervace().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getStavRezervace().getValue()).append("</ID>");
                xml.append("</StavRezervace>");
            }

            // StavSmlouva
            if (data.getStavSmlouva() != null) {
                xml.append("<StavSmlouva>");
                xml.append("<Nazev>").append(data.getStavSmlouva().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getStavSmlouva().getValue()).append("</ID>");
                xml.append("</StavSmlouva>");
            }

            // TypDoprava
            if (data.getTypDoprava() != null && data.getTypDoprava().getValue() != null) {
                com.example.objednavkasoapclient.IntegerNazev typDoprava = data.getTypDoprava().getValue();
                xml.append("<TypDoprava>");

                // Append the Nazev if available
                if (typDoprava.getNazev() != null && typDoprava.getNazev().getValue() != null) {
                    xml.append("<Nazev>").append(typDoprava.getNazev().getValue()).append("</Nazev>");
                } else {
                    xml.append("<Nazev/>");
                }

                // Append the ID if available
                if (typDoprava.getID() != null) {
                    xml.append("<ID>").append(typDoprava.getID()).append("</ID>");
                } else {
                    xml.append("<ID/>");
                }

                xml.append("</TypDoprava>");
            } else {
                // If TypDoprava is null, include an empty TypDoprava element
                xml.append("<TypDoprava/>");
            }

            // TypStrava
            if (data.getTypStrava() != null) {
                xml.append("<TypStrava>");
                xml.append("<Nazev>").append(data.getTypStrava().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getTypStrava().getValue()).append("</ID>");
                xml.append("</TypStrava>");
            }
            if (data.getRezervaceDopravy() != null && data.getRezervaceDopravy().getValue() != null) {
                ArrayOfRezervaceDoprava rezervaceDopravy = data.getRezervaceDopravy().getValue();
                xml.append("<RezervaceDoprava>");
                xml.append("<CasNastupni>").append(rezervaceDopravy.getRezervaceDoprava().get(1).getCasNastupni()).append("</CasNastupni>");
                xml.append("<CasVystupni>").append(rezervaceDopravy.getRezervaceDoprava().get(1).getCasVystupni()).append("</CasVystupni>");

                // Safely extract and append LetisteNastupni
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteNastupniElement = rezervaceDopravy.getRezervaceDoprava().get(1).getLetisteNastupni();
                if (letisteNastupniElement != null && letisteNastupniElement.getValue() != null) {
                    com.example.objednavkasoapclient.IntegerNazev letisteNastupni = letisteNastupniElement.getValue();
                    if (letisteNastupni.getNazev() != null && letisteNastupni.getNazev().getValue() != null) {
                        xml.append("<LetisteNastupni>").append(letisteNastupni.getNazev().getValue()).append("</LetisteNastupni>");
                    } else {
                        xml.append("<LetisteNastupni/>");
                    }
                }

// Safely extract and append LetisteVystupni
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteVystupniElement = rezervaceDopravy.getRezervaceDoprava().get(1).getLetisteVystupni();
                if (letisteVystupniElement != null && letisteVystupniElement.getValue() != null) {
                    IntegerNazev letisteVystupni = letisteVystupniElement.getValue();
                    if (letisteVystupni.getNazev() != null && letisteVystupni.getNazev().getValue() != null) {
                        xml.append("<LetisteVystupni>").append(letisteVystupni.getNazev().getValue()).append("</LetisteVystupni>");
                    } else {
                        xml.append("<LetisteVystupni/>");
                    }
                }

                xml.append("</RezervaceDoprava>");
            }

        }

        xml.append("</ObjednavkaDetailResult>");
        xml.append("</ObjednavkaDetailResponse>");
        xml.append("</s:Body>");
        xml.append("</s:Envelope>");

        return xml.toString();
    }
    private <T > String getJAXBElementValue(JAXBElement < T > element) {
        return element != null && element.getValue() != null ? element.getValue().toString() : "";
    }
}
