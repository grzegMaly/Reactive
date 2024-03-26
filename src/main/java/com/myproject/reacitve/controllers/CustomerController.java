package com.myproject.reacitve.controllers;

import com.myproject.reacitve.model.CustomerDTO;
import com.myproject.reacitve.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;


    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers() {

        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {

        return customerService.getCustomerById(customerId);
    }

    @PostMapping(CUSTOMER_PATH)
    ResponseEntity<Void> createNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {

        AtomicInteger atomicInteger = new AtomicInteger();

        customerService.createCustomer(customerDTO).subscribe(savedCustomer -> {
            atomicInteger.set(savedCustomer.getId());
        });

        return ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl("http://localhost:8080/" + CUSTOMER_PATH + "/" + atomicInteger.get())
                        .build().toUri())
                .build();
    }

    @PutMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> updateCustomerById(@PathVariable("customerId") Integer customerId,
                                            @Validated @RequestBody CustomerDTO customerDTO) {

        customerService.updateCustomer(customerId, customerDTO).subscribe();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> patchCustomerById(@PathVariable("customerId") Integer customerId,
                                           @Validated @RequestBody CustomerDTO customerDTO) {

        customerService.patchCustomer(customerId, customerDTO).subscribe();

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {

        customerService.deleteCustomer(customerId).subscribe();

        return ResponseEntity.noContent().build();
    }
}
