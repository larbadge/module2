package com.mikhalov.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Television extends Device {

    private int diagonal;
    private String country;

    public Television(String series, String screenType, int price, int diagonal, String country) {
        super(series, screenType, price, DeviceType.TELEVISION);
        this.diagonal = diagonal;
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("Television: series %s, screen type %s, diagonal %d, country %s, price %d", getSeries(), getScreenType(), getDiagonal(), getCountry(), getPrice());
    }

}
