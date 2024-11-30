package com.backend.profis_service;

import com.backend.auth.EncryptionUtil;
import com.backend.entity.*;
import com.backend.repository.*;
import com.example.klientsoapclient.*;
import com.example.klientsoapclient.Klient;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.objednavkasoapclient.*;
import com.example.objednavkasoapclient.IntegerNazev;
import com.example.objednavkasoapclient.Objednavka;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository tourOrderRepository;
    @Autowired
    private TransportationReservationRepository transportationReservationRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AccommodationReservationRepository accommodationReservationRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

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
    @Autowired
    private OrderUserRepository orderUserRepository;


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

    public ObjednavkaDetailResult ObjednavkaDetail(long id){
        ObjednavkaContext context = new ObjednavkaContext();
        context.setUzivatelHeslo(passwordElement);    // Set the user's password
        context.setUzivatelLogin(usernameElement);    // Set the user's login
        context.setVypsatNazvy(true);               // Set to false as per request
        context.setIdJazyk(idElement);
        // Optional<OrderUser> optionalOrder = orderUserRepository.getLatestOrderByUserId(id);
        //OrderUser orderUser = optionalOrder.get(); // Unwrap the Optional safely
        //   OrderUserId orderUserId = orderUser.getId(); // Get the composite key
            int orderId = 8291; // Extract orderId from the composite key
            String klic = "D88A537D"; // Get the `klic` field

            // Use `orderId` and `klic` as needed
            System.out.println("Order ID: " + orderId);
            System.out.println("Klic: " + klic);
            context.setIdObjednavka(orderId);
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
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(order.getID());// Example order number
                System.out.println(order.getID() + "AAAAAAAA");
                String klicValue = getJAXBElementValue(order.getKlic());
                // Create a new OrderUser
                OrderUser orderUser = new OrderUser(orderDetail, user, klicValue);
                orderDetail.setOrderUsers(orderUser);

                // Save the TourOrder (cascades to OrderUser)
                tourOrderRepository.save(orderDetail);
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

    public String createOrder(ObjednavkaDetailResult result) {
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
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(data.getID());
            orderDetail.setAdults(data.getDospelych());
            orderDetail.setChildren(data.getDeti());
            orderDetail.setInfants(data.getInfantu());
            orderDetail.setNumberOfNights(data.getNoci());
            orderDetail.setName(getJAXBElementValue(data.getNazev()));
            orderDetail.setEndDate(toLocalDateTime(data.getDatumDo().getValue()));
            orderDetail.setStartDate(toLocalDateTime(data.getDatumOd().getValue()));
            orderDetail.setPaymentStatus(getJAXBElementValue(data.getStavPlatba().getValue().getNazev()));
            orderDetail.setCurrency(getJAXBElementValue(data.getMena()));
            orderDetail.setStateOfOrder(getJAXBElementValue(data.getStavObjednavka()));
            //orderDetail.setReservationStatus(data.getStavRezervace() != null ? data.getStavRezervace().getName() : null);
            orderDetailRepository.save(orderDetail);
            // Save transportation reservations
            List<TransportationReservation> transportations = new ArrayList<>();
            if (data.getRezervaceDopravy() != null && data.getRezervaceDopravy().getValue() != null) {
                for (RezervaceDoprava transportation : data.getRezervaceDopravy().getValue().getRezervaceDoprava()) {
                    TransportationReservation transportReservation = new TransportationReservation();
                    transportReservation.setId(transportation.getID());
                    transportReservation.setPickupTime(toLocalDateTime(transportation.getCasNastupni()));
                    transportReservation.setDropoffTime(toLocalDateTime(transportation.getCasVystupni()));
                    transportReservation.setStartDate(toLocalDateTime(transportation.getDatum()));

                    JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteVystupniElement = transportation.getLetisteVystupni();
                    if (letisteVystupniElement != null && letisteVystupniElement.getValue() != null) {
                        IntegerNazev letisteVystupni = letisteVystupniElement.getValue();
                        if (letisteVystupni.getNazev() != null && letisteVystupni.getNazev().getValue() != null) {
                            transportReservation.setArrivalAirportName(letisteVystupni.getNazev().getValue());
                        } else {
                        }
                    }
                    JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteNastupniElement = transportation.getLetisteNastupni();
                    if (letisteNastupniElement != null && letisteNastupniElement.getValue() != null) {
                        IntegerNazev letisteNastupni = letisteNastupniElement.getValue();
                        if (letisteNastupni.getNazev() != null && letisteNastupni.getNazev().getValue() != null) {
                            transportReservation.setDepartureAirportName(letisteNastupni.getNazev().getValue());
                        } else {
                        }
                    }
                    transportReservation.setTransportId(transportation.getIdDoprava());
                    transportReservation.setRouteName(transportation.getSmer().value());
                    transportReservation.setTransportType(transportation.getTypDoprava().getValue().getID());
                    transportations.add(transportReservation);

                }
                transportationReservationRepository.saveAll(transportations);
            }
            orderDetail.setTransportationReservations(transportations);

            // Save accommodation reservations
            List<AccommodationReservation> accommodations = new ArrayList<>();
            if (data.getRezervaceUbytovani() != null && data.getRezervaceUbytovani().getValue() != null) {
                for (RezervaceUbytovani accommodation : data.getRezervaceUbytovani().getValue().getRezervaceUbytovani()) {
                    AccommodationReservation accReservation = new AccommodationReservation();
                    accReservation.setId(accommodation.getID());
                    accReservation.setStartDate(toLocalDateTime(accommodation.getDatumOd()));
                    accReservation.setNumberOfNights(accommodation.getNoci());
                    accReservation.setBeds(accommodation.getLuzek());
                    accReservation.setExtraBeds(accommodation.getPristylek());
                    accReservation.setAccommodationName(getJAXBElementValue(accommodation.getNazevUbytovani()));
                    accReservation.setNote(getJAXBElementValue(accommodation.getPoznamka()));
                    accReservation.setOrderDetail(orderDetail);

                    // Save associated hotel
                    Hotel hotel = new Hotel();
                    if (accommodation.getObjednavkaHotel() != null) {
                        hotel.setId(accommodation.getObjednavkaHotel().getValue().getID());
                        hotel.setName(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getNazev()));
                        hotelRepository.save(hotel);
                    }
                    accReservation.setObjednavkaHotel(hotel);
                    accommodations.add(accReservation);
                }
                accommodationReservationRepository.saveAll(accommodations);
            }
            orderDetail.setAccommodationReservations(accommodations);

            // Save OrderDetail
            orderDetailRepository.save(orderDetail);

            xml.append("<Data>");
            xml.append("<ObjednavkaDetail>");
            xml.append("<ID>").append(data.getID()).append("</ID>");
            xml.append("</ObjednavkaDetail>");
            xml.append("</Data>");
        }

        xml.append("</ObjednavkaDetailResult>");
        xml.append("</ObjednavkaDetailResponse>");
        xml.append("</s:Body>");
        xml.append("</s:Envelope>");

        return xml.toString();}

    private <T > String getJAXBElementValue(JAXBElement < T > element) {
        return element != null && element.getValue() != null ? element.getValue().toString() : "";
    }

    public static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
            if (xmlGregorianCalendar == null) {
                return null; // Handle null case
            }
            return xmlGregorianCalendar.toGregorianCalendar()
                    .toZonedDateTime()
                    .toLocalDateTime();
        }
}


