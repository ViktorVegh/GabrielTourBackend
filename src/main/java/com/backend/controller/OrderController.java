package com.backend.controller;

import com.backend.auth.JwtHelper;
import com.backend.profis_service.OrderService;
import com.example.klientsoapclient.*;
import com.example.klientsoapclient.KlientKontakt;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.objednavkasoapclient.*;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/get-order")
    public ResponseEntity<String> objednavkaListResult(@RequestBody Map<String, Object> body) {
        try {
            int id = Integer.parseInt(body.get("id").toString()); // Extract ID from the Map
            System.out.println("I got to controller with ID: " + id);

            // Fetch order data using the service
            KlientObjednavkaListResult result = orderService.klientObjednavkaList(id);

            // Convert the result to XML
            String xmlResponse = buildXmlResponse(result);

            // Return the XML response
            return ResponseEntity.ok()
                    .header("Content-Type", "application/xml")
                    .body(xmlResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "<s:Body>" +
                            "<Error>Error processing request.</Error>" +
                            "</s:Body>" +
                            "</s:Envelope>");
        }
    }
    @PostMapping("/get-orderDetails")
    public ResponseEntity<String> objednavkaDetailResult(@RequestBody Map<String, Object> body) {
        try {
            int id = Integer.parseInt(body.get("id").toString()); // Extract ID from the Map
            System.out.println("I got to controller with ID: " + id);

            // Fetch order data using the service
            ObjednavkaDetailResult result = orderService.ObjednavkaDetail(id);

            // Convert the result to XML
            String xmlResponse = buildXmlResponse2(result);

            // Return the XML response
            return ResponseEntity.ok()
                    .header("Content-Type", "application/xml")
                    .body(xmlResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "<s:Body>" +
                            "<Error>Error processing request.</Error>" +
                            "</s:Body>" +
                            "</s:Envelope>");
        }
    }

    private String buildXmlResponse(KlientObjednavkaListResult result) {
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
                xml.append("<CenaCelkem>").append(order.getCenaCelkem() != null ? order.getCenaCelkem() : "0.00").append("</CenaCelkem>");
                xml.append("<DatumOd>").append(getJAXBElementValue(order.getDatumOd())).append("</DatumOd>");
                xml.append("<DatumDo>").append(getJAXBElementValue(order.getDatumDo())).append("</DatumDo>");
                xml.append("<Dospelych>").append(order.getDospelych() != null ? order.getDospelych() : 0).append("</Dospelych>");
                xml.append("<Klic>").append(order.getKlic()).append("</Klic>");
                xml.append("<Deti>").append(order.getDeti() != null ? order.getDeti() : 0).append("</Deti>");
                xml.append("<Noci>").append(order.getNoci() != null ? order.getNoci() : 0).append("</Noci>");
                xml.append("<Nazev>").append(getJAXBElementValue(order.getNazev())).append("</Nazev>");

                // Nested Klient object
                if (order.getKlient() != null && order.getKlient().getValue() != null) {
                    KlientKontakt klient = order.getKlient().getValue();
                    xml.append("<Klient>");
                    xml.append("<ID>").append(klient.getID()).append("</ID>");
                    xml.append("<Email>").append(klient.getEmail()).append("</Email>");
                    xml.append("<Jmeno>").append(klient.getJmeno()).append("</Jmeno>");
                    xml.append("<Prijmeni>").append(klient.getPrijmeni()).append("</Prijmeni>");
                    xml.append("</Klient>");
                }

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
    private String buildXmlResponse2(ObjednavkaDetailResult result) {
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
            xml.append("<Nazev>").append(data.getNazev()).append("</Nazev>");

            // Letiste
            if (data.getLetiste() != null) {
                xml.append("<Letiste>");
                xml.append("<Nazev>").append(data.getLetiste().getName()).append("</Nazev>");
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

            // StavPokyny
            if (data.getStavPokyny() != null) {
                xml.append("<StavPokyny>");
                xml.append("<Nazev>").append(data.getStavPokyny().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getStavPokyny().getValue()).append("</ID>");
                xml.append("</StavPokyny>");
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
            if (data.getTypDoprava() != null) {
                xml.append("<TypDoprava>");
                xml.append("<Nazev>").append(data.getTypDoprava().getName()).append("</Nazev>");
                xml.append("<ID>").append(data.getTypDoprava().getValue()).append("</ID>");
                xml.append("</TypDoprava>");
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
                xml.append("<LetisteNastupni>").append(rezervaceDopravy.getRezervaceDoprava().get(1).getLetisteNastupni().getName()).append("</LetisteNastupni>");
                xml.append("<LetisteVystupni>").append(rezervaceDopravy.getRezervaceDoprava().get(1).getLetisteVystupni().toString()).append("</LetisteVystupni>");
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
