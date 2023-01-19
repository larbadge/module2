package com.mikhalov.service;

import com.mikhalov.model.Device;
import com.mikhalov.model.Invoice;
import com.mikhalov.repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    private static final InvoiceRepository INVOICE_REPOSITORY = InvoiceRepository.getInstance();

    public void getAnalytics() {
        System.out.println("Analytics:");
        System.out.println("Sold devices: " + getInfoOfSoldDevices());
        System.out.println("_".repeat(50));
        Invoice lowestInvoice = getLowestInvoice();
        System.out.printf("Lowest invoice amount %d$, %s%n", lowestInvoice.getTotalAmount(), lowestInvoice.getCustomer());
        System.out.println("_".repeat(50));
        System.out.printf("Sum of invoices total amount : %d$%n", getSumOfAllInvoicesTotalAmount());
        System.out.println("_".repeat(50));
        System.out.println("Count of retail invoices " + getCountOfRetailInvoices());
        System.out.println("_".repeat(50));
        System.out.println("Invoices that includes single device type: ");
        getInvoicesIncludesSingleDeviceType().forEach(System.out::println);
        System.out.println("_".repeat(50));
        System.out.println("First three invoices :");
        getFirstThreeInvoices().forEach(System.out::println);
        System.out.println("_".repeat(50));
        System.out.println("Invoice by customers under eighteen: ");
        getInvoicesUnderEighteen().forEach(System.out::println);
        System.out.println("_".repeat(50));
        List<List<Invoice>> listsWithSortedInvoices = sortInvoices();
        System.out.println("Invoices sorted by customer age: ");
        listsWithSortedInvoices.get(0).forEach(System.out::println);
        System.out.println("_".repeat(50));
        System.out.println("Invoices sorted by devices count: ");
        listsWithSortedInvoices.get(1).forEach(System.out::println);
        System.out.println("_".repeat(50));
        System.out.println("Invoices sorted by total amount: ");
        listsWithSortedInvoices.get(2).forEach(System.out::println);
        System.out.println("_".repeat(50));

    }

    private Map<Device.DeviceType, Long> getInfoOfSoldDevices() {
        return INVOICE_REPOSITORY.getAll().stream()
                .flatMap(v -> v.getDevices().stream())
                .collect(Collectors.groupingBy(Device::getDeviceType, Collectors.counting()));
    }

    private Invoice getLowestInvoice() {
        return INVOICE_REPOSITORY.getAll().stream()
                .min(Comparator.comparingInt(Invoice::getTotalAmount)).orElseThrow();

    }

    private int getSumOfAllInvoicesTotalAmount() {
        return INVOICE_REPOSITORY.getAll().stream()
                .mapToInt(Invoice::getTotalAmount).sum();
    }

    private int getCountOfRetailInvoices() {
        return (int) INVOICE_REPOSITORY.getAll().stream()
                .filter(invoice -> invoice.getInvoiceType().equals(Invoice.InvoiceType.RETAIL))
                .count();
    }

    private List<Invoice> getInvoicesIncludesSingleDeviceType() {
        return INVOICE_REPOSITORY.getAll().stream()
                .filter(invoice -> invoice.getDevices().stream()
                        .map(Device::getDeviceType)
                        .distinct().count() == 1)
                .collect(Collectors.toList());
    }

    private List<Invoice> getFirstThreeInvoices() {
        return INVOICE_REPOSITORY.getAll().stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<Invoice> getInvoicesUnderEighteen() {
        return INVOICE_REPOSITORY.getAll().stream()
                .filter(invoice -> invoice.getCustomer().getAge() < 18)
                .peek(invoice -> invoice.setInvoiceType(Invoice.InvoiceType.LOW_AGE))
                .collect(Collectors.toList());
    }

    private List<List<Invoice>> sortInvoices() {
        ArrayList<Invoice> invoicesByAge = new ArrayList<>();
        ArrayList<Invoice> invoicesByDevicesCount = new ArrayList<>();
        ArrayList<Invoice> invoicesByTotalAmount = new ArrayList<>();

        INVOICE_REPOSITORY.getAll().stream()
                .sorted(Comparator.comparingInt(i -> -i.getCustomer().getAge()))
                .peek(invoicesByAge::add)
                .sorted(Comparator.comparingInt(i -> -i.getDevices().size()))
                .peek(invoicesByDevicesCount::add)
                .sorted(Comparator.comparingInt(Invoice::getTotalAmount).reversed())
                .forEach(invoicesByTotalAmount::add);

        return List.of(invoicesByAge, invoicesByDevicesCount, invoicesByTotalAmount);
    }
    
}
