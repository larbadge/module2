package com.mikhalov.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Customer {

    private final String id;
    private final String email;
    private final int age;

    public Customer(String email, int age) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Customer: id %s, email %s, age %d", id, email, age);
    }
}
