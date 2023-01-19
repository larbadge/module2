package com.mikhalov.repository;

import com.mikhalov.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    private static InvoiceRepository invoiceRepository;
    private final List<Invoice> invoices = new ArrayList<>();

    private InvoiceRepository() {

    }

    public static InvoiceRepository getInstance() {
        if (invoiceRepository == null) {
            invoiceRepository = new InvoiceRepository();
        }
        return invoiceRepository;
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    public List<Invoice> getAll() {
        return List.copyOf(invoices);
    }
}
