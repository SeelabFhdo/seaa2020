package org.example.location.service.Location.interfaces;

import org.example.location.domain.Location.Location;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"location"})
public class ProvidedTypes {

    @GetMapping(value = "destination")
    protected Location getDestination() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping(value = "from")
    protected Location getFrom() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping(value = "to")
    protected Location getTo() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
