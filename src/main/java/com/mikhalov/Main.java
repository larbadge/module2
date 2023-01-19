package com.mikhalov;

import com.mikhalov.service.ShopService;

public class Main {
    public static void main(String[] args) {
        ShopService shopService = ShopService.getInstance();
        shopService.addDevicesFromCsv("positions.csv");
        for (int i = 0; i < 15; i++) {
            shopService.getRandomInvoice();
        }

    }
}
