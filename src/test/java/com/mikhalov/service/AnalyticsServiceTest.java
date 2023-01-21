package com.mikhalov.service;

import com.mikhalov.model.Device;
import com.mikhalov.model.Invoice;
import com.mikhalov.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsServiceTest {

    static List<Invoice> testInvoices;
    InvoiceRepository invoiceRepository;
    AnalyticsService analyticsService;

    @BeforeAll
    static void beforeAll() {
        testInvoices = TestDataGenerator.generateTestInvoices();
    }

    @BeforeEach
    void setUp() {
        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        Mockito.when(invoiceRepository.getAll()).thenReturn(testInvoices);
        analyticsService = new AnalyticsService(invoiceRepository);
    }

    @Test
    void testGetInfoOfSoldDevices() {
        Map<Device.DeviceType, Long> result = analyticsService.getInfoOfSoldDevices();
        assertEquals(4, result.get(Device.DeviceType.TELEPHONE));
        assertEquals(4, result.get(Device.DeviceType.TELEVISION));
    }

    @Test
    void testGetLowestInvoice() {
        Invoice expected = testInvoices.get(2);
        Invoice result = analyticsService.getLowestInvoice();
        assertEquals(expected.getTotalAmount(), result.getTotalAmount());
        assertEquals(expected.getCustomer(), result.getCustomer());
    }

    @Test
    void testGetSumOfAllInvoicesTotalAmount() {
        int result = analyticsService.getSumOfAllInvoicesTotalAmount();
        assertEquals(10000, result);
    }

    @Test
    void testGetCountOfRetailInvoices() {
        int result = analyticsService.getCountOfRetailInvoices();
        assertEquals(4, result);
    }

    @Test
    void testGetInvoicesIncludesSingleDeviceType() {
        List<Invoice> result = analyticsService.getInvoicesIncludesSingleDeviceType();
        assertEquals(2, result.size());
        assertEquals(Device.DeviceType.TELEPHONE, result.get(0).getDevices().get(0).getDeviceType());
        assertEquals(Device.DeviceType.TELEVISION, result.get(1).getDevices().get(0).getDeviceType());
    }

    @Test
    void testGetFirstThreeInvoices() {
        List<Invoice> expected = List.of(testInvoices.get(0), testInvoices.get(1), testInvoices.get(2));
        List<Invoice> result = analyticsService.getFirstThreeInvoices();
        assertEquals(3, result.size());
        assertEquals(expected, result);
    }

    @Test
    void testGetInvoicesUnderEighteen() {
        List<Invoice> expected = List.of(testInvoices.get(0));
        List<Invoice> result = analyticsService.getInvoicesUnderEighteen();
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    void testSortInvoices() {
        ArrayList<List<Invoice>> expected = new ArrayList<>();
        expected.add(List.of(testInvoices.get(2), testInvoices.get(1),testInvoices.get(3), testInvoices.get(0)));
        expected.add(List.of(testInvoices.get(1), testInvoices.get(0),testInvoices.get(2), testInvoices.get(3)));
        expected.add(List.of(testInvoices.get(1), testInvoices.get(0),testInvoices.get(3), testInvoices.get(2)));
        List<List<Invoice>> result = analyticsService.sortInvoices();
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
    }

}