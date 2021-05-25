package com.andrianovartemii.timetable;

import org.json.simple.JSONObject;

public class ShipCargo {

    protected CargoType type;
    public int weight;

    public CargoType getType() {
        return type;
    }

    public ShipCargo(CargoType type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    public ShipCargo(JSONObject object) {
        switch (object.get("type").toString()) {
            case "LiquidCargo" -> this.type = CargoType.LiquidCargo;
            case "Containers" -> this.type = CargoType.Containers;
            default -> this.type = CargoType.BulkCargo;
        }
        this.weight = Integer.parseInt(object.get("weight").toString());
    }

    @Override
    public String toString() {
        return "type= " + type + ", weight= " + weight;
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("type", type.toString());
        object.put("weight", weight);
        return object;
    }
}
