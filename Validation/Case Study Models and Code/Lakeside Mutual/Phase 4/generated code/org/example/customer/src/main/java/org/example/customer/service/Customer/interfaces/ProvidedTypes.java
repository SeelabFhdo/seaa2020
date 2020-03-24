package org.example.customer.service.Customer.interfaces;

import org.example.customer.domain.customer.CustomerId;
import org.example.customer.domain.customer.ListCustomerId;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"customer"})
public class ProvidedTypes {

    @GetMapping(value = "customerId")
    protected CustomerId getCustomerId() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping(value = "customers")
    protected ListCustomerId getCustomers() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
