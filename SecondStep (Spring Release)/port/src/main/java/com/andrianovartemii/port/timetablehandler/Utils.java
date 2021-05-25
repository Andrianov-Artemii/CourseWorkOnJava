package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;

public class Utils {
    public static LocalDateTime convertDateTime(String date) {
        DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd:HH:mm")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.YEAR, 1993)
                .toFormatter(Locale.ENGLISH);
        return LocalDateTime.parse(date, parseFormatter);
    }

    public static CargoType convertCargoType(String cargoType) {
        switch (cargoType) {
            case "BulkCargo":
                return CargoType.BulkCargo;
            case "LiquidCargo":
                return CargoType.LiquidCargo;
            default:
                return CargoType.Containers;
        }
    }

    public static TimetableElement convertTimetableElement(JSONObject jsonObject) {
        LocalDateTime arrivingTime = Utils.convertDateTime(jsonObject.get("arrivingTime").toString());
        JSONObject ship = (JSONObject) jsonObject.get("ship");
        String shipName = ship.get("shipName").toString();
        CargoType cargoType = Utils.convertCargoType(((JSONObject) ship.get("shipCargo")).get("cargoType").toString());
        int cargoWeight = Integer.parseInt(((JSONObject) ship.get("shipCargo")).get("weight").toString());
        return new TimetableElement(arrivingTime, shipName, cargoType, cargoWeight);
    }

    public static Properties readProperties() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    public static JSONArray getJsonArray(LinkedList<TimetableElement> list)
    {
        JSONArray jsonArray = new JSONArray();
        for (var el : list) {
            jsonArray.add(el.getJsonObject());
        }
        return jsonArray;
    }

}
