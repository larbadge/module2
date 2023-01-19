package com.mikhalov.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device {
    private final String series;
    private final String screenType;
    private int price;
    private final DeviceType deviceType;

    public Device(String series, String screenType, int price, DeviceType deviceType) {
        this.series = series;
        this.screenType = screenType;
        this.price = price;
        this.deviceType = deviceType;
    }

    public enum DeviceType {
        TELEPHONE,
        TELEVISION
    }

}
