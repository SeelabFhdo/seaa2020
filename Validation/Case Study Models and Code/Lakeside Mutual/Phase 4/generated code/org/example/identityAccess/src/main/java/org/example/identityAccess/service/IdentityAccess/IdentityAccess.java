package org.example.identityAccess.service.IdentityAccess;

import de.fhdo.lemma.msa.services.LemmaMicroservice;
import de.fhdo.lemma.msa.services.LemmaMicroserviceType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@LemmaMicroservice(type = LemmaMicroserviceType.FUNCTIONAL)
@SpringBootApplication()
public class IdentityAccess {

    public static void main(String[] args) {
        SpringApplication.run(IdentityAccess.class, args);
    }
}
