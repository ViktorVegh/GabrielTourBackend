package com.backend.service;

import com.backend.dtos.EntityToDTOMapper;
import com.backend.dtos.OrderDTO;
import com.backend.entity.*;
import com.backend.repository.*;
import com.backend.service_interface.OrderServiceInterface;
import com.example.klientsoapclient.*;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.objednavkasoapclient.*;
import com.example.objednavkasoapclient.IntegerNazev;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class OrderService implements OrderServiceInterface {
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

    @Override
    public String createOrderList(KlientObjednavkaListResult result, Long id) {
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
                    System.out.println("present"+existingOrderDetail.get().getId());
                    orderDetail = existingOrderDetail.get();
                } else {
                    // If it does not exist, create a new one
                    orderDetail = new OrderDetail();
                    orderDetail.setId(order.getID()); // Example order number
                    orderDetail.setStartDate(toLocalDateTime(order.getDatumOd().getValue()));
                    orderDetail.setEndDate(toLocalDateTime(order.getDatumDo().getValue()));
                    System.out.println("not present"+orderDetail.getId());
                    orderDetailRepository.save(orderDetail); // Save the new order detail

                    // Only create an OrderUser if the OrderDetail is newly created
                    String klicValue = getJAXBElementValue(order.getKlic());
                    OrderUser orderUser = new OrderUser(orderDetail, user, klicValue);
                    orderDetail.setOrderUsers(orderUser);
                }

                // Save the TourOrder (cascades to OrderUser)
                orderDetailRepository.save(orderDetail);
            }
            xml.append("</Data>");
        }
        return xml.toString();
    }

    @Override
    @Transactional
    public int createOrderDetail(ObjednavkaDetailResult result) {
        StringBuilder xml = new StringBuilder();
        int resultid=0;
        if (result != null && result.getData() != null && result.getData().getValue() != null) {
            ObjednavkaPopis data = result.getData().getValue();

            // Basic fields
            OrderDetail orderDetail = orderDetailRepository.getReferenceById(result.getData().getValue().getID());
            orderDetail.setId(data.getID());
            orderDetail.setAdults(data.getDospelych());
            orderDetail.setChildren(data.getDeti()!= null ? data.getDeti() : 0);
            orderDetail.setInfants(data.getInfantu()!= null ? data.getInfantu() : 0);
            orderDetail.setNumberOfNights(data.getNoci()!= null ? data.getNoci() : 0);
            orderDetail.setName(getJAXBElementValue(data.getNazev()));
            orderDetail.setEndDate(data.getDatumDo() != null && data.getDatumDo().getValue() != null
                    ? toLocalDateTime(data.getDatumDo().getValue())
                    : null);
            orderDetail.setStartDate(data.getDatumOd() != null && data.getDatumOd().getValue() != null
                    ? toLocalDateTime(data.getDatumOd().getValue())
                    : null);
            orderDetail.setPaymentStatus(data.getStavPlatba() != null && data.getStavPlatba().getValue() != null
                    ? getJAXBElementValue(data.getStavPlatba().getValue().getNazev())
                    : null);
            orderDetail.setCurrency(
                    data.getMena() != null && data.getMena().getValue() != null && data.getMena().getValue().getNazev() != null
                            ? getJAXBElementValue(data.getMena().getValue().getNazev())
                            : null
            );
            orderDetail.setStateOfOrder(
                    data.getStavObjednavka() != null && data.getStavObjednavka().getValue() != null && data.getStavObjednavka().getValue().getNazev() != null
                            ? getJAXBElementValue(data.getStavObjednavka().getValue().getNazev())
                            : null
            );
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
            List<RezervaceDoprava> reservations =
                    result != null && result.getData() != null && result.getData().getValue() != null &&
                            result.getData().getValue().getRezervaceDopravy() != null &&
                            result.getData().getValue().getRezervaceDopravy().getValue() != null
                            ? result.getData().getValue().getRezervaceDopravy().getValue().getRezervaceDoprava()
                            : Collections.emptyList(); // Return an empty list if any part is null

            // Step 2: Extract IDs from the reservations list
            List<Integer> ids = reservations.stream()
                    .map(RezervaceDoprava::getID)
                    .filter(Objects::nonNull) // Filter out null IDs to prevent errors
                    .collect(Collectors.toList());

            // Step 3: Fetch existing transportations and map them by ID
            List<TransportationReservation> existingTransportations = transportationReservationRepository.findAllById(ids);
            Map<Integer, TransportationReservation> existingTransportationsMap = existingTransportations.stream()
                    .collect(Collectors.toMap(TransportationReservation::getId, transport -> transport));

            // Step 4: Create a list for updated transportation entities
            List<TransportationReservation> updatedTransportations = new ArrayList<>(existingTransportations);

            for (RezervaceDoprava transportation : reservations) {
                if (transportation == null) continue; // Skip null reservations

                TransportationReservation transportEntity = existingTransportationsMap.getOrDefault(transportation.getID(), new TransportationReservation());
                transportEntity.setId(transportation.getID());
                transportEntity.setPickupTime(transportation.getCasNastupni() != null ? toLocalDateTime(transportation.getCasNastupni()) : null);
                transportEntity.setDropoffTime(transportation.getCasVystupni() != null ? toLocalDateTime(transportation.getCasVystupni()) : null);
                transportEntity.setStartDate(transportation.getDatum() != null ? toLocalDateTime(transportation.getDatum()) : null);
                transportEntity.setTransportId(transportation.getIdDoprava() != null ? transportation.getIdDoprava() : null);
                transportEntity.setRouteName(transportation.getSmer() != null ? transportation.getSmer().value() : null);
                transportEntity.setTransportType(
                        transportation.getTypDoprava() != null && transportation.getTypDoprava().getValue() != null
                                ? transportation.getTypDoprava().getValue().getID()
                                : null
                );
                transportEntity.setOrderDetail(orderDetail);
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteVystupniElement = transportation.getLetisteVystupni();
                if (letisteVystupniElement != null && letisteVystupniElement.getValue() != null) {
                    IntegerNazev letisteVystupni = letisteVystupniElement.getValue();
                    transportEntity.setArrivalAirportName(
                            letisteVystupni.getNazev() != null && letisteVystupni.getNazev().getValue() != null
                                    ? letisteVystupni.getNazev().getValue()
                                    : null
                    );
                }

                // Handle Departure Airport Name
                JAXBElement<com.example.objednavkasoapclient.IntegerNazev> letisteNastupniElement = transportation.getLetisteNastupni();
                if (letisteNastupniElement != null && letisteNastupniElement.getValue() != null) {
                    IntegerNazev letisteNastupni = letisteNastupniElement.getValue();
                    transportEntity.setDepartureAirportName(
                            letisteNastupni.getNazev() != null && letisteNastupni.getNazev().getValue() != null
                                    ? letisteNastupni.getNazev().getValue()
                                    : null
                    );
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
            // Step 1: Extract the list of ObjednavkaCena objects
            List<ObjednavkaCena> listOfPrices = result != null && result.getData() != null && result.getData().getValue() != null &&
                    result.getData().getValue().getCeny() != null &&
                    result.getData().getValue().getCeny().getValue() != null
                    ? result.getData().getValue().getCeny().getValue().getObjednavkaCena()
                    : Collections.emptyList(); // Return an empty list if any part is null

            // Step 2: Extract IDs from the list of prices
            List<Integer> ids2 = listOfPrices.stream()
                    .map(ObjednavkaCena::getID) // Assuming ObjednavkaCena has a method getID()
                    .filter(Objects::nonNull) // Filter out null IDs to prevent issues
                    .collect(Collectors.toList());

            // Fetch existing Prices entities
            List<Prices> prices3 = priceRepository.findAllById(ids2);

            // Map existing Prices entities by ID
            Map<Integer, Prices> existingPricesMap = prices3.stream()
                    .collect(Collectors.toMap(Prices::getId, price -> price));

            // Prepare a list for updated prices
            List<Prices> updatedPrices = new ArrayList<>(prices3);

            // Update or create Prices entities
            if (data.getCeny() != null && data.getCeny().getValue() != null) {
                List<ObjednavkaCena> cenyList = data.getCeny().getValue().getObjednavkaCena();
                if (cenyList != null) {
                    for (ObjednavkaCena price : cenyList) {
                        if (price == null) continue; // Skip null entries

                        Prices priceEntity = existingPricesMap.getOrDefault(price.getID(), new Prices());

                        // Update fields safely
                        priceEntity.setId(price.getID());
                        priceEntity.setPrice(price.getCena() != null ? price.getCena() : BigDecimal.ZERO); // Default to 0 if null
                        priceEntity.setName(price.getNazev() != null ? getJAXBElementValue(price.getNazev()) : null);
                        priceEntity.setCurrency(
                                data.getMena() != null && data.getMena().getValue() != null
                                        ? getJAXBElementValue(data.getMena().getValue().getNazev())
                                        : null
                        );
                        priceEntity.setOrderDetail(orderDetail);
                        priceEntity.setQuantity(price.getPocet() != null ? price.getPocet() : 0); // Default to 0 if null

                        // Only add new entities to updatedPrices
                        if (!existingPricesMap.containsKey(price.getID())) {
                            updatedPrices.add(priceEntity);
                        }
                    }
                }
            }
            orderDetail.getPrices().clear();
            for (Prices priceEntity : updatedPrices) {
                priceEntity.setOrderDetail(orderDetail);
                orderDetail.getPrices().add(priceEntity);
            }
            // Step 1: Extract the list of RezervaceUbytovani objects
            List<RezervaceUbytovani> listOfAccommodations = result != null && result.getData() != null && result.getData().getValue() != null &&
                    result.getData().getValue().getRezervaceUbytovani() != null &&
                    result.getData().getValue().getRezervaceUbytovani().getValue() != null
                    ? result.getData().getValue().getRezervaceUbytovani().getValue().getRezervaceUbytovani()
                    : Collections.emptyList(); // Return an empty list if any part is null

            // Step 2: Extract IDs from the accommodations list
            List<Integer> ids3 = listOfAccommodations.stream()
                    .map(RezervaceUbytovani::getID) // Assuming RezervaceUbytovani has a method getID()
                    .filter(Objects::nonNull) // Filter out null IDs to avoid issues
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
            resultid = data.getID();
        }

        return resultid;}

    private <T > String getJAXBElementValue(JAXBElement < T > element) {
        return element != null && element.getValue() != null ? element.getValue().toString() : "";
    }

    @Override
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
    static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null; // Handle null case
        }
        return xmlGregorianCalendar.toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDateTime();
    }




}


