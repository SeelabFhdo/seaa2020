package org.example.city.service.City.interfaces;

import org.example.city.domain.city.ListCityShared;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"city"})
public class ProvidedTypes {

    @GetMapping(value = "cities")
    protected ListCityShared getCitiesForPostalCode(String postalCode) {
        checkRequiredParametersOfGetCitiesForPostalCode(postalCode);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void checkRequiredParametersOfGetCitiesForPostalCode(String postalCode) {
        if (postalCode == null)
            throw new IllegalArgumentException("Required parameter \"postalCode\" must not be null");
    }
}
