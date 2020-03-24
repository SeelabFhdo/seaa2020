package org.example.cargo.service.Cargo.interfaces;

import org.example.cargo.domain.Cargo.DeliverySpecification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"cargo"})
public class ProvidedTypes {

    @GetMapping(value = "delivery")
    protected DeliverySpecification getDeliverySpecification() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
