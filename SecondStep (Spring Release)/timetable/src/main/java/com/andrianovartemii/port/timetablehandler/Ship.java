package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONObject;

public class Ship implements JsonSerializable {

    private String shipName;
    private Cargo shipCargo;
    private int shipWeight;

    public Ship(String shipName, Cargo shipCargo, int shipWeight) {
        this.shipName = shipName;
        this.shipCargo = shipCargo;
        this.shipWeight = shipWeight;
    }

    @Override
    public String toString() {
        return "Ship " + shipName + " with " + shipCargo.toString() + " on the board";
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shipName", shipName);
        jsonObject.put("shipWeight", shipWeight);
        jsonObject.put("shipCargo", shipCargo.getJsonObject());
        return jsonObject;
    }

    private static int shipId = 0;

    public static Ship getRandomObject() {
        return new Ship("Ship [Number " + (shipId++) + "]", Cargo.getRandomObject(), 100 + (int) (Math.random() * 200));
    }
}
