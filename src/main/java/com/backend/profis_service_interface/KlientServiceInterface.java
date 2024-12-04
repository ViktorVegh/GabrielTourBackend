package com.backend.profis_service_interface;

import com.example.klientsoapclient.KlientDataInput;
import com.example.klientsoapclient.RegistrovatResult;

public interface KlientServiceInterface {
    void initialize() throws Exception;

    // Create JAXBElement values for each field and set them
    KlientDataInput setKlientDataInput() throws Exception;

    RegistrovatResult Registrovat();
}
