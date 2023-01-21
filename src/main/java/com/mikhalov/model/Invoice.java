package com.mikhalov.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Invoice {

    private InvoiceType invoiceType;
    private final Customer customer;
    private final List<Device> devices;
    private final int totalAmount;
    private final String invoiceId;
    private final LocalDateTime time;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int INVOICE_AMOUNT_LIMIT = 5000;

    public Invoice(Customer customer, List<Device> devices) {
        this.time = LocalDateTime.now();
        this.invoiceId = UUID.randomUUID().toString();
        this.customer = customer;
        this.devices = devices;
        this.totalAmount = devices.stream().mapToInt(Device::getPrice).sum();
        if (totalAmount > INVOICE_AMOUNT_LIMIT) {
            this.invoiceType = InvoiceType.WHOLESALE;
        } else {
            this.invoiceType = InvoiceType.RETAIL;
        }
    }

    @Override
    public String toString() {
        return String.format("Invoice %s:%n\tTime: %s;%n\t%s;%n\tOrder: %s;%n\tInvoice type %s;%n\tTotal amount %d$",
                invoiceId,
                time.format(FORMATTER),
                customer,
                devices.stream().map(Device::toString).
                        collect(Collectors.joining(System.lineSeparator() + "\t\t")),
                invoiceType,
                totalAmount);
    }

    public enum InvoiceType {
        WHOLESALE,
        RETAIL,
        LOW_AGE
    }

}
