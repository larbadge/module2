package com.mikhalov.repository;

import com.mikhalov.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceRepository {

    private final List<Device> devices = new ArrayList<>();
    private static DeviceRepository deviceRepository;

    private DeviceRepository() {

    }

    public static DeviceRepository getInstance() {
        if (deviceRepository == null) {
            deviceRepository = new DeviceRepository();
        }
        return deviceRepository;
    }

    public void save(Device device) {
        devices.add(device);
    }

    public void saveAll(List<Device> devices) {
        this.devices.addAll(devices);
    }

    public List<Device> getAll() {
        return new ArrayList<>(devices);
    }

    public void clear() {
        devices.clear();
    }

}
