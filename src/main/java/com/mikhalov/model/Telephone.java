package com.mikhalov.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Telephone extends Device {

    private String model;

    public Telephone(String series, String screenType, int price, String model) {
        super(series, screenType, price, DeviceType.TELEPHONE);
        this.model = model;

    }

    @Override
    public String toString() {
        return String.format("Telephone: series %s, model %s, screen type %s, price %d", getSeries(), getModel(), getScreenType(), getPrice());
    }

}
