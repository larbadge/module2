package com.mikhalov.service;

import com.mikhalov.model.Customer;
import com.mikhalov.model.Device;
import com.mikhalov.model.Invoice;
import com.mikhalov.repository.InvoiceRepository;
import com.mikhalov.util.RandomGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShopService {

    private static final DeviceService DEVICE_SERVICE = DeviceService.getInstance();
    private static final InvoiceRepository INVOICE_REPOSITORY = InvoiceRepository.getInstance();
    private final PersonService personService = new PersonService();
    private final AnalyticsService analyticsService = new AnalyticsService();
    private static ShopService shopService;

    private ShopService() {

    }

    public static ShopService getInstance() {
        if (shopService == null) {
            shopService = new ShopService();
        }
        return shopService;
    }

    public Invoice getInvoice(Customer customer, List<Device> devices) {
        Invoice invoice = new Invoice(customer, devices);
        order(invoice);
        return invoice;

    }

    public Invoice getRandomInvoice() {
        Customer customer = personService.getRandomCustomer();
        List<Device> devices = getRandomSizeAndShuffledListOfDevices();
        return getInvoice(customer, devices);
    }

    private void order(Invoice invoice) {
        INVOICE_REPOSITORY.addInvoice(invoice);
        System.out.println(invoice);
        if (INVOICE_REPOSITORY.getAll().size() % 15 == 0) {
            analyticsService.getAnalytics();
        }
    }

    public void addDevicesFromCsv(String fileInResources) {
        DEVICE_SERVICE.addFromCsv(fileInResources);
    }

    public List<Invoice> getAllInvoices() {
        return INVOICE_REPOSITORY.getAll();
    }

    private List<Device> getRandomSizeAndShuffledListOfDevices() {
        List<Device> devices = DEVICE_SERVICE.getAll();
        Collections.shuffle(devices);
        return devices.stream()
                .limit(RandomGenerator.getRandomIntForOrder())
                .collect(Collectors.toList());
    }


}
