package com.backend.profis_service_interface;

import com.example.klientsoapclient.*;

public interface LoginServiceInterface {
    KlientPrihlasitResult login(String email, String password);

    OveritEmailResult overitEmail(String email);

    ResetHeslaOdeslatResult resetHeslaOdeslat(int clientId);

    ResetHeslaOveritResult resetHeslaOverit(String authKey, String email, int clientId);

    ZmenitHesloResult changePassword(String authKey, String password, int clientId, String email);
}
