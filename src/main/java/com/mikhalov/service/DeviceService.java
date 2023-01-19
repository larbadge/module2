package com.mikhalov.service;

import com.mikhalov.model.Device;
import com.mikhalov.model.Telephone;
import com.mikhalov.model.Television;
import com.mikhalov.repository.DeviceRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeviceService {

    private static final DeviceRepository DEVICE_REPOSITORY = DeviceRepository.getInstance();
    private static DeviceService deviceService;
    private int typeIndex;
    private int seriesIndex;
    private int screenTypeIndex;
    private int priceIndex;
    private int modelIndex;
    private int diagonalIndex;
    private int countryIndex;

    private DeviceService() {

    }

    public static DeviceService getInstance() {
        if (deviceService == null) {
            deviceService = new DeviceService();
        }
        return deviceService;
    }

    public void add(Device device) {
        DEVICE_REPOSITORY.save(device);
    }

    public void deleteAll() {
        DEVICE_REPOSITORY.clear();
    }

    public List<Device> getAll() {
        return DEVICE_REPOSITORY.getAll();
    }

    public void addFromCsv(String path) {
        List<Device> devices = createListOfDevicesFromCsv(path);
        DEVICE_REPOSITORY.saveAll(devices);
    }

    private List<Device> createListOfDevicesFromCsv(String path) {
        List<List<String>> lists = readFile(path);
        List<String> columns = lists.remove(0);
        typeIndex = columns.indexOf("type");
        seriesIndex = columns.indexOf("series");
        screenTypeIndex = columns.indexOf("screen type");
        priceIndex = columns.indexOf("price");
        modelIndex = columns.indexOf("model");
        diagonalIndex = columns.indexOf("diagonal");
        countryIndex = columns.indexOf("country");
        return lists.stream().map(this::createDevice).collect(Collectors.toList());

    }

    private Device createDevice(List<String> list) {
        Device.DeviceType deviceType = Device.DeviceType.valueOf(list.get(typeIndex).toUpperCase());
        if (deviceType.equals(Device.DeviceType.TELEPHONE)) {
            return createTelephone(list);
        } else
            return createTelevision(list);
    }

    private Device createTelephone(List<String> list) {
        String series = list.get(seriesIndex);
        String screenType = list.get(screenTypeIndex);
        int price = Integer.parseInt(list.get(priceIndex));
        String model = list.get(modelIndex);
        return new Telephone(series, screenType, price, model);
    }

    private Device createTelevision(List<String> list) {
        String series = list.get(seriesIndex);
        String screenType = list.get(screenTypeIndex);
        int price = Integer.parseInt(list.get(priceIndex));
        int diagonal = Integer.parseInt(list.get(diagonalIndex));
        String country = list.get(countryIndex);
        return new Television(series, screenType, price, diagonal, country);
    }

    private List<List<String>> readFile(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String delimiter = ",";
        List<List<String>> lists = new ArrayList<>();
        try (InputStream input = loader.getResourceAsStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(input)))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(delimiter));
                try {
                    boolean b = values.stream().anyMatch(String::isBlank);
                    if (b) {
                        throw new WrongRawException("Wrong raw was not added to list");//TODO create exception
                    } else {
                        lists.add(values);
                    }
                } catch (WrongRawException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }
}
