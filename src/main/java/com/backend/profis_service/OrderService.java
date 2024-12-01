package com.backend.profis_service;

import com.backend.auth.EncryptionUtil;
import com.backend.dtos.OrderDTO;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository tourOrderRepository;
    @Autowired
    private PriceRepository priceRepository;
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
        String FinalResult = createOrderList(result,id);
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
    private String createOrderList(KlientObjednavkaListResult result,Long id) {
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
                /*
                KlientObjednavkaListResult existingOrderUser = orderUserRepository;
                Map<Integer, TransportationReservation> existingTransportationsMap = existingTransportations.stream()
                        .collect(Collectors.toMap(TransportationReservation::getId, transport -> transport));
                List<TransportationReservation> updatedTransportations = new ArrayList<>(existingTransportations);
    ;           */
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

    public String createOrderDetail(ObjednavkaDetailResult result) {
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
            OrderDetail orderDetail = orderDetailRepository.getReferenceById(result.getData().getValue().getID());
            orderDetail.setId(data.getID());
            orderDetail.setAdults(data.getDospelych());
            orderDetail.setChildren(data.getDeti());
            orderDetail.setInfants(data.getInfantu());
            orderDetail.setNumberOfNights(data.getNoci());
            orderDetail.setName(getJAXBElementValue(data.getNazev()));
            orderDetail.setEndDate(toLocalDateTime(data.getDatumDo().getValue()));
            orderDetail.setStartDate(toLocalDateTime(data.getDatumOd().getValue()));
            orderDetail.setPaymentStatus(getJAXBElementValue(data.getStavPlatba().getValue().getNazev()));
            orderDetail.setCurrency(getJAXBElementValue(data.getMena().getValue().getNazev()));
            orderDetail.setStateOfOrder(getJAXBElementValue(data.getStavObjednavka()));

            // Step 1: Extract the list of RezervaceDoprava objects
            List<RezervaceDoprava> reservations = result.getData().getValue()
                    .getRezervaceDopravy().getValue()
                    .getRezervaceDoprava();

            // Step 2: Extract IDs from the reservations list
            List<Integer> ids = reservations.stream()
                    .map(RezervaceDoprava::getID)
                    .collect(Collectors.toList());

            List<TransportationReservation> existingTransportations = transportationReservationRepository.findAllById(ids);
            Map<Integer, TransportationReservation> existingTransportationsMap = existingTransportations.stream()
                    .collect(Collectors.toMap(TransportationReservation::getId, transport -> transport));
            List<TransportationReservation> updatedTransportations = new ArrayList<>(existingTransportations);

            for (RezervaceDoprava transportation : data.getRezervaceDopravy().getValue().getRezervaceDoprava()) {
                TransportationReservation transportEntity = existingTransportationsMap.getOrDefault(transportation.getID(), new TransportationReservation());
                transportEntity.setId(transportation.getID());
                transportEntity.setPickupTime(toLocalDateTime(transportation.getCasNastupni()));
                transportEntity.setDropoffTime(toLocalDateTime(transportation.getCasVystupni()));
                transportEntity.setStartDate(toLocalDateTime(transportation.getDatum()));
                transportEntity.setTransportId(transportation.getIdDoprava());
                transportEntity.setRouteName(transportation.getSmer().value());
                transportEntity.setTransportType(transportation.getTypDoprava().getValue().getID());
                transportEntity.setOrderDetail(orderDetail);
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteVystupniElement = transportation.getLetisteVystupni();
                if (letisteVystupniElement != null && letisteVystupniElement.getValue() != null) {
                    IntegerNazev letisteVystupni = letisteVystupniElement.getValue();
                    if (letisteVystupni.getNazev() != null && letisteVystupni.getNazev().getValue() != null) {
                        transportEntity.setArrivalAirportName(letisteVystupni.getNazev().getValue());
                    } else {
                    }
                }
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteNastupniElement = transportation.getLetisteNastupni();
                if (letisteNastupniElement != null && letisteNastupniElement.getValue() != null) {
                    IntegerNazev letisteNastupni = letisteNastupniElement.getValue();
                    if (letisteNastupni.getNazev() != null && letisteNastupni.getNazev().getValue() != null) {
                        transportEntity.setDepartureAirportName(letisteNastupni.getNazev().getValue());
                    } else {
                    }
                }

                // Additional mappings...
                if (!existingTransportationsMap.containsKey(transportation.getID())) {
                    updatedTransportations.add(transportEntity);
                }
            }
            orderDetail.getTransportationReservations().clear();
            for (TransportationReservation transportEntity : updatedTransportations) {
                transportEntity.setOrderDetail(orderDetail);
                orderDetail.getTransportationReservations().add(transportEntity);
            }
            // Step 1: Extract the list of RezervaceDoprava objects
            List<ObjednavkaCena> ListofPrices = result.getData().getValue().getCeny().getValue().getObjednavkaCena();

            // Step 2: Extract IDs from the reservations list
            List<Integer> ids2 = ListofPrices.stream()
                    .map(ObjednavkaCena::getID) // Assuming RezervaceDoprava has a method getId()
                    .collect(Collectors.toList());

            // Fetch existing Prices entities
            List<Prices> prices3 = priceRepository.findAllById(ids2);

            // Map existing Prices entities by ID
            Map<Integer, Prices> existingPricesMap = prices3.stream()
                    .collect(Collectors.toMap(Prices::getId, price -> price));

            // Prepare a list for updated prices
            List<Prices> updatedPrices = new ArrayList<>(prices3);

            // Update or create Prices entities
            for (ObjednavkaCena price : data.getCeny().getValue().getObjednavkaCena()) {
                Prices priceEntity = existingPricesMap.getOrDefault(price.getID(), new Prices());

                // Update fields
                priceEntity.setId(price.getID());
                priceEntity.setPrice(price.getCena());
                priceEntity.setName(getJAXBElementValue(price.getNazev()));
                priceEntity.setCurrency(getJAXBElementValue(data.getMena().getValue().getNazev()));
                priceEntity.setOrderDetail(orderDetail);

                // Only add new entities to updatedPrices
                if (!existingPricesMap.containsKey(price.getID())) {
                    updatedPrices.add(priceEntity);
                }
            }
            orderDetail.getPrices().clear();
            for (Prices priceEntity : updatedPrices) {
                priceEntity.setOrderDetail(orderDetail);
                orderDetail.getPrices().add(priceEntity);
            }
            // Step 1: Extract the list of RezervaceDoprava objects
            List<RezervaceUbytovani> ListofAcccomodations = result.getData().getValue().getRezervaceUbytovani().getValue().getRezervaceUbytovani();

            // Step 2: Extract IDs from the reservations list
            List<Integer> ids3 = ListofAcccomodations.stream()
                    .map(RezervaceUbytovani::getID)
                    .collect(Collectors.toList());

            List<AccommodationReservation> accommodations = accommodationReservationRepository.findAllById(ids3);

            // Map existing Accomodation entities by ID
            Map<Integer, AccommodationReservation> existingAccomodationsMap = accommodations.stream()
                    .collect(Collectors.toMap(AccommodationReservation::getId, accommodation -> accommodation));

            // Prepare a list for updated prices
            List<AccommodationReservation> updatedAccomodations = new ArrayList<>(accommodations);

            if (data.getRezervaceUbytovani() != null && data.getRezervaceUbytovani().getValue() != null) {
                for (RezervaceUbytovani accommodation : data.getRezervaceUbytovani().getValue().getRezervaceUbytovani()) {
                    AccommodationReservation accReservation = existingAccomodationsMap.getOrDefault(accommodation.getID(), new AccommodationReservation());
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
                        //hotel.setProfisId(accommodation.getObjednavkaHotel().getValue().getID());
                        hotel.setName(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getNazev()));
                        hotelRepository.save(hotel);
                    }
                    accReservation.setObjednavkaHotel(hotel);
                    accommodations.add(accReservation);
                    // Only add new entities to updatedPrices
                    if (!existingAccomodationsMap.containsKey(accommodation.getID())) {
                        updatedAccomodations.add(accReservation);
                    }
                }

            }

            orderDetail.getAccommodationReservations().clear();

            for (AccommodationReservation accomodationEntity : updatedAccomodations) {
                accomodationEntity.setOrderDetail(orderDetail);
                orderDetail.getAccommodationReservations().add(accomodationEntity);
            }
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

    public OrderDTO getObjednavkaDetail(int id) {
        OrderUserId userId = new OrderUserId();
        userId.setOrderId(id);
        //Optional<OrderUser> orderUser = orderUserRepository.findById(userId);
        //int Orderid = orderUser.get().getOrderDetail().getId();
        OrderDetail orderDetail= orderDetailRepository.getReferenceById(8291);
        //List<TransportationReservation> transportationReservation = transportationReservationRepository.findByOrderDetail_Id(8291);
        //List<AccommodationReservation> accommodationReservation= accommodationReservationRepository.findByOrderDetail_Id(8291);
        //List<Prices> prices = priceRepository.findByOrderDetail_Id(8291);
        OrderDTO orderDTO= new OrderDTO(orderDetail);
        return orderDTO;
    }
}


