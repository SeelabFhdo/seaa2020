package org.example.interactionLog.service.InteractionLog.interfaces;

import org.example.interactionLog.domain.interactionLog.ListNotification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"interactionLog"})
public class InteractionLog {

    @GetMapping(value = "notifications")
    protected ListNotification getNotifications() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
