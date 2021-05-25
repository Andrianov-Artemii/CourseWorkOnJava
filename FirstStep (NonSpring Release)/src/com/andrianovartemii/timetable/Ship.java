package com.andrianovartemii.timetable;

import org.json.simple.JSONObject;

public class Ship {
    protected String name;
    public ShipCargo cargo;
    public int weight;

    public String getName() { return name; };

    public Ship(String name, ShipCargo cargo, int weight)
    {
        this.name = name;
        this.cargo = cargo;
        this.weight = weight;
    }

    public Ship(JSONObject object)
    {
        this.name = object.get("name").toString();
        this.cargo = new ShipCargo((JSONObject) object.get("cargo"));
        this.weight = Integer.parseInt(object.get("weight").toString());

    }

    @Override
    public String toString() {
        return "name= '" + name + '\'' +
                ", cargo= " + cargo +
                ", weight= " + weight +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("cargo", cargo.toJSONObject());
        object.put("weight", weight);
        return object;
    }
}
