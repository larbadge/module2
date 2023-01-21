package com.mikhalov.service;

import com.mikhalov.model.Customer;
import com.mikhalov.model.Device;
import com.mikhalov.model.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataGenerator {

    private static final DeviceService DEVICE_SERVICE = DeviceService.getInstance();

    public static List<Invoice> generateTestInvoices() {
        DEVICE_SERVICE.addFromCsv("test.csv");
        List<Device> devices = DEVICE_SERVICE.getAll();
        ArrayList<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(
                new Customer("Customer1@mail.com", 15),
                devices));
        invoices.add(new Invoice(
                new Customer("Customer2@mail.com", 24),
                Stream.concat(devices.stream(), devices.stream())
                        .collect(Collectors.toList())));
        invoices.add(new Invoice(
                new Customer("Customer3@mail.com", 28),
                devices.stream().limit(1).collect(Collectors.toList())));
        invoices.add(new Invoice(
                new Customer("Customer4@mail.com", 18),
                devices.stream().skip(1).collect(Collectors.toList())));

        return invoices;
    }
}
