package com.backend.controller;

import com.backend.auth.JwtHelper;
import com.backend.profis_service.OrderService;
import com.example.klientsoapclient.KlientKontakt;
import com.example.klientsoapclient.KlientObjednavkaListResult;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.klientsoapclient.ResetHeslaOveritResult;
import com.example.objednavkasoapclient.ObjednavkaDetailResult;
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

    private <T> String getJAXBElementValue(JAXBElement<T> element) {
        return element != null && element.getValue() != null ? element.getValue().toString() : "";
    }



}
