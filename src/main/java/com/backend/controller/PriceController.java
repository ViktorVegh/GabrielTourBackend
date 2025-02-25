package com.backend.controller;


import com.backend.entity.Prices;
import com.backend.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping
    public int createPrice(@RequestBody Prices price) {
        return priceService.createPrice(price);
    }

    @GetMapping("/{id}")
    public Prices getPriceById(@PathVariable int id) {
        return priceService.getPriceById(id);
    }
}
