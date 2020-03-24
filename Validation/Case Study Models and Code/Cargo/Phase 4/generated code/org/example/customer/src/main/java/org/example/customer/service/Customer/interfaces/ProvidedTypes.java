package org.example.customer.service.Customer.interfaces;

import org.example.customer.domain.Customer.CustomerShared;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"customer"})
public class ProvidedTypes {

    @GetMapping(value = "customer")
    protected CustomerShared getCustomer() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
