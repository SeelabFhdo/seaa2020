package org.example.policy.service.Policy.interfaces;

import org.example.policy.domain.policy.PolicyAggregateRoot;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component()
@RestController()
@RequestMapping(value = {"policy"})
public class Policy {

    @PutMapping(value = "updatePolicy")
    protected void updatePolicy(PolicyAggregateRoot policy) {
        checkRequiredParametersOfUpdatePolicy(policy);
    }

    private void checkRequiredParametersOfUpdatePolicy(PolicyAggregateRoot policy) {
        if (policy == null)
            throw new IllegalArgumentException("Required parameter \"policy\" must not be null");
    }
}
