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

    private final InvoiceRepository invoiceRepository;

    public AnalyticsService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

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

    public Map<Device.DeviceType, Long> getInfoOfSoldDevices() {
        return invoiceRepository.getAll().stream()
                .flatMap(v -> v.getDevices().stream())
                .collect(Collectors.groupingBy(Device::getDeviceType, Collectors.counting()));
    }

    public Invoice getLowestInvoice() {
        return invoiceRepository.getAll().stream()
                .min(Comparator.comparingInt(Invoice::getTotalAmount)).orElseThrow();

    }

    public int getSumOfAllInvoicesTotalAmount() {
        return invoiceRepository.getAll().stream()
                .mapToInt(Invoice::getTotalAmount).sum();
    }

    public int getCountOfRetailInvoices() {
        return (int) invoiceRepository.getAll().stream()
                .filter(invoice -> invoice.getInvoiceType().equals(Invoice.InvoiceType.RETAIL))
                .count();
    }

    public List<Invoice> getInvoicesIncludesSingleDeviceType() {
        return invoiceRepository.getAll().stream()
                .filter(invoice -> invoice.getDevices().stream()
                        .map(Device::getDeviceType)
                        .distinct().count() == 1)
                .collect(Collectors.toList());
    }

    public List<Invoice> getFirstThreeInvoices() {
        return invoiceRepository.getAll().stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoicesUnderEighteen() {
        return invoiceRepository.getAll().stream()
                .filter(invoice -> invoice.getCustomer().getAge() < 18)
                .peek(invoice -> invoice.setInvoiceType(Invoice.InvoiceType.LOW_AGE))
                .collect(Collectors.toList());
    }

    public List<List<Invoice>> sortInvoices() {
        List<Invoice> invoicesByAge = new ArrayList<>();
        List<Invoice> invoicesByDevicesCount = new ArrayList<>();
        List<Invoice> invoicesByTotalAmount = new ArrayList<>();

        invoiceRepository.getAll().stream()
                .sorted(Comparator.comparingInt(i -> -i.getCustomer().getAge()))
                .peek(invoicesByAge::add)
                .sorted(Comparator.comparingInt(i -> -i.getDevices().size()))
                .peek(invoicesByDevicesCount::add)
                .sorted(Comparator.comparingInt(Invoice::getTotalAmount).reversed())
                .forEach(invoicesByTotalAmount::add);

        return List.of(invoicesByAge, invoicesByDevicesCount, invoicesByTotalAmount);
    }
    
}
