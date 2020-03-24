package org.example.identityAccess.service.IdentityAccess.interfaces;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"identityAccess"})
public class IdentityAccess {

    @PostMapping(value = "login")
    private void performLogin(String email, String password) {
        checkRequiredParametersOfPerformLogin(email, password);
    }

    private void checkRequiredParametersOfPerformLogin(String email, String password) {
        if (email == null)
            throw new IllegalArgumentException("Required parameter \"email\" must not be null");
        if (password == null)
            throw new IllegalArgumentException("Required parameter \"password\" must not be null");
    }
}
