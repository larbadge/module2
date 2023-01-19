package com.mikhalov.service;

import com.mikhalov.model.Customer;
import com.mikhalov.util.RandomGenerator;

public class PersonService {

    public Customer getRandomCustomer() {
        final String email = RandomGenerator.getRandomString() + "@mail.com";
        final int age = RandomGenerator.getRandomAge();

        return new Customer(email, age);
    }
}
