package com.backend.service;

import com.backend.auth.EncryptionUtil;
import com.backend.dtos.EntityToDTOMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
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

    @Autowired
    private OrderUserRepository orderUserRepository;

    public OrderService() {
    }

    public String createOrderList(KlientObjednavkaListResult result,Long id) {
        StringBuilder xml = new StringBuilder();
        if (result != null && result.getData() != null && result.getData().getValue() != null) {
            xml.append("<Data>");
            for (ObjednavkaKlient order : result.getData().getValue().getObjednavkaKlient()) {
                xml.append("<ID>").append(order.getID()).append("</ID>");
                xml.append("<Klic>").append(getJAXBElementValue(order.getKlic())).append("</Klic>");

                User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));

                OrderDetail orderDetail;

                // Check if the order detail exists
                Optional<OrderDetail> existingOrderDetail = orderDetailRepository.findById(order.getID());

                if (existingOrderDetail.isPresent()) {
                    // If it exists, retrieve the existing order detail
                    orderDetail = existingOrderDetail.get();
                } else {
                    // If it does not exist, create a new one
                    orderDetail = new OrderDetail();
                    orderDetail.setId(order.getID()); // Example order number
                    orderDetailRepository.save(orderDetail); // Save the new order detail

                    // Only create an OrderUser if the OrderDetail is newly created
                    String klicValue = getJAXBElementValue(order.getKlic());
                    OrderUser orderUser = new OrderUser(orderDetail, user, klicValue);
                    orderDetail.setOrderUsers(orderUser);
                }

                // Save the TourOrder (cascades to OrderUser)
                orderDetailRepository.save(orderDetail);
                xml.append("</ObjednavkaKlient>");
            }
            xml.append("</Data>");
        }
        return xml.toString();
    }

    public String createOrderDetail(ObjednavkaDetailResult result) {
        StringBuilder xml = new StringBuilder();

        if (result != null && result.getData() != null && result.getData().getValue() != null) {
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
            orderDetail.setStateOfOrder(getJAXBElementValue(data.getStavObjednavka().getValue().getNazev()));

            List<String> travelerIds = data.getCestujici() != null && data.getCestujici().getValue() != null
                    ? data.getCestujici().getValue().getCestujici().stream()
                    .map(cestujici -> {
                        if (cestujici.getKlient() != null && cestujici.getKlient().getValue() != null) {
                            return String.valueOf(cestujici.getKlient().getValue().getID()); // Convert ID to String
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
                    : Collections.emptyList();

            orderDetail.setTravelers(travelerIds);

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
                priceEntity.setQuantity(price.getPocet());

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
                    accReservation.setMealType(getJAXBElementValue(accommodation.getTypStrava().getValue().getNazev()));
                    accReservation.setOrderDetail(orderDetail);


                    // Save associated hotel
                    Hotel hotel = new Hotel();
                    if (accommodation.getObjednavkaHotel() != null) {
                        //hotel.setProfisId(accommodation.getObjednavkaHotel().getValue().getID());
                        hotel.setName(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getNazev()));
                        hotel.setCountry(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getStat().getValue().getNazev()));
                        hotel.setStars(accommodation.getObjednavkaHotel().getValue().getHvezdy());
                        hotel.setArea(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getStredisko().getValue().getNazev()));
                        hotel.setRegion(getJAXBElementValue(accommodation.getObjednavkaHotel().getValue().getOblast().getValue().getNazev()));
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
            xml.append("<ID>").append(data.getID()).append("</ID>");
            xml.append("</Data>");
        }

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

    public OrderDTO getOrderDetail(int id) {
        OrderUserId userId = new OrderUserId();
        userId.setUserId((long) id);

        // Fetch the order user object
        OrderUser orderUser = orderUserRepository.findClosestOrderByUserId(userId.getUserId());

        // Check if orderUser is null and throw an exception
        if (orderUser == null) {
            throw new IllegalArgumentException("No order details found for user ID: " + id);
        }
        OrderDTO dto = EntityToDTOMapper.mapToOrderDTO(orderUser.getOrderDetail());;
        // Return the order details
        return dto;
    }
}


