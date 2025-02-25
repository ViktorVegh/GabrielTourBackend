package com.backend.service;


import com.backend.entity.Services.Prices;
import com.backend.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    /**
     * Create a new price and return its ID
     */
    public int createPrice(Prices price) {
        Prices savedPrice = priceRepository.save(price);
        return savedPrice.getId();
    }

    /**
     * Retrieve a price by ID
     */
    public Prices getPriceById(int priceId) {
        return priceRepository.findById(priceId)
                .orElseThrow(() -> new IllegalArgumentException("Price not found with ID: " + priceId));
    }


    public List<Prices> getAllPrices() {
        return priceRepository.findAll();
    }
}
