package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONObject;

import java.util.Random;

public class Cargo implements JsonSerializable {
    private CargoType cargoType;
    private int weight;

    public Cargo(CargoType cargoType, int weight) {
        this.cargoType = cargoType;
        this.weight = weight;
    }

    public Cargo(JSONObject jsonObject) {
        switch (jsonObject.get("cargoType").toString()) {
            case "BulkCargo" -> this.cargoType = CargoType.BulkCargo;
            case "LiquidCargo" -> this.cargoType = CargoType.LiquidCargo;
            case "Containers" -> this.cargoType = CargoType.Containers;
            default -> this.cargoType = CargoType.BulkCargo;
        }
        weight = (int) jsonObject.get("weight");
    }

    @Override
    public String toString() {
        return cargoType + " (" + weight + ")";
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cargoType", cargoType);
        jsonObject.put("weight", weight);
        return jsonObject;
    }

    public static Cargo getRandomObject() {
        int cargoCount = (int) (Math.random() * 3);
        CargoType type;
        switch (cargoCount) {
            case 0 -> type = CargoType.BulkCargo;
            case 1 -> type = CargoType.LiquidCargo;
            default -> type = CargoType.Containers;
        }
        return new Cargo(type, 20 + (int) (Math.random() * 1000));
    }

}
