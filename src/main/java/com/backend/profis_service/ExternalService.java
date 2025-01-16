package com.backend.profis_service;

import com.backend.profis_service_interface.ExternalServiceInterface;
import com.backend.profis_service_interface.ProfisOrderServiceInterface;
import com.example.klientsoapclient.Klient;
import com.example.objednavkasoapclient.Objednavka;
import com.example.ostatnisoapclient.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.Service;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.crypto.Data;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ExternalService implements ExternalServiceInterface {

    private static final String WSDL_URL = "https://xml.gabrieltour.sk/API/v1/Ostatni.svc?wsdl";
    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "OstatniService");
    private static final QName PORT_NAME = new QName("http://tempuri.org/", "BasicHttpBinding_Ostatni");
    private static final String NAMESPACE_URI = "http://xml.profis.profitour.cz";
    private String passwordUser="aplikacia";
    private String usernameUser="jbabica@2";
    private String id="01";

    JAXBElement<String> passwordElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelHeslo"), String.class, passwordUser);
    JAXBElement<String> usernameElement = new JAXBElement<>(new QName(NAMESPACE_URI, "UzivatelLogin"), String.class, usernameUser);
    JAXBElement<String> idElement = new JAXBElement<>(new QName(NAMESPACE_URI, "id_Jazyk"), String.class, id);
    JAXBElement<String> setKeyOrder = new JAXBElement<>(new QName(NAMESPACE_URI, "Key"), String.class, "id_Objednavka");
    JAXBElement<String> setKeyKey = new JAXBElement<>(new QName(NAMESPACE_URI, "Key"), String.class, "Klic");
    private Ostatni ostatniPort;


    public ExternalService() throws Exception {
        // Initialize the klient service and port
        URL wsdlLocation = new URL(WSDL_URL);
        Service service = Service.create(wsdlLocation, SERVICE_NAME);
        ostatniPort = service.getPort(PORT_NAME, Ostatni.class);

    }
    public ArrayList<String> getAirportCodes(int id,String key){
        Context context = new Context();
        context.setUzivatelHeslo(passwordElement); // Set the user's password
        context.setUzivatelLogin(usernameElement); // Set the user's login
        context.setVypsatNazvy(true);              // Set to false as per request
        context.setIdJazyk(idElement);
        ExterniInput externiInput = new ExterniInput();
        ObjectFactory factory = new ObjectFactory();

        // Set Nazev
        externiInput.setNazev(factory.createExterniInputNazev("ObjednavkaDopravaExtra"));

        // Create and populate parameters
        ArrayOfPair parameters = new ArrayOfPair();
        List<Pair> pairs = parameters.getPair();

        // Create first parameter pair (id_Objednavka)
        Pair idPair = new Pair();
        JAXBElement<String> OrderId = new JAXBElement<>(new QName(NAMESPACE_URI, "Value"), String.class, String.valueOf(id));
        idPair.setKey(setKeyOrder);
        idPair.setValue(OrderId);
        pairs.add(idPair);

        // Create second parameter pair (Klic)
        Pair klicPair = new Pair();
        JAXBElement<String> orderKey = new JAXBElement<>(new QName(NAMESPACE_URI, "Value"), String.class, key);
        klicPair.setKey(setKeyKey);
        klicPair.setValue(orderKey);

        pairs.add(klicPair);

        // Set parameters to ExterniInput
        externiInput.setParametry(factory.createExterniInputParametry(parameters));

        // Make the SOAP call
        ExterniProceduraResult result = ostatniPort.externiProcedura(context, externiInput);
        ArrayList<String> finalResult = decodeAirport(result);
        return finalResult;
    }
    public ArrayList<String> decodeAirport(ExterniProceduraResult result) {
        ArrayList<String> cisloList = new ArrayList<>();

        try {
            // Extract the table list from the result (List<Table>)
            List<com.example.ostatnisoapclient.Table> tables = result.getData().getValue().getTable();

            // Ensure there is at least one table
            if (tables.isEmpty()) {
                System.out.println("No tables found in the result.");
                return cisloList; // Return empty list if no tables found
            }

            // Get the first Table (Assuming it contains the rows)
            com.example.ostatnisoapclient.Table table = tables.get(0);

            // Get the list of rows, assuming getRows() returns a JAXBElement<RowList>
            JAXBElement<RowList> rowListElement = table.getRows(); // Get rows as JAXBElement<RowList>

            if (rowListElement == null || rowListElement.getValue() == null) {
                System.out.println("RowList is empty or null.");
                return cisloList; // Return empty list if RowList is null
            }

            RowList rowList = rowListElement.getValue();  // Extract the value (RowList)

            // Ensure that the rowList has data
            if (rowList.getRow().isEmpty()) {
                System.out.println("No rows found in RowList.");
                return cisloList; // Return empty list if no rows found
            }

            // Loop through the rows in RowList (assuming each row is an instance of StringList)
            for (Object row : rowList.getRow()) {
                // Print the row to debug the content
                System.out.println("Processing row: " + row);

                // If row is an instance of StringList, extract the data
                if (row instanceof StringList) {
                    StringList stringList = (StringList) row; // Cast to StringList
                    System.out.println("Row size: " + stringList.getItem().size()); // Log the size of StringList

                    // Check if the StringList has enough items (at least 5 items)
                    if (stringList.getItem().size() > 4) {
                        // Retrieve the 'Cislo' (5th Item, index 4)
                        String cislo = stringList.getItem().get(4); // Get the 5th string (Cislo)
                        cisloList.add(cislo); // Add 'Cislo' to the list
                    } else {
                        System.out.println("Row does not have enough items. Skipping this row.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as necessary
        }

        return cisloList;
    }

}
