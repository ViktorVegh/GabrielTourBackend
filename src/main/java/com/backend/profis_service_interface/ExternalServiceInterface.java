package com.backend.profis_service_interface;

import com.example.ostatnisoapclient.ExterniProceduraResult;

import java.util.ArrayList;

public interface ExternalServiceInterface {
    ArrayList<String> getAirportCodes(int id, String key);
}
