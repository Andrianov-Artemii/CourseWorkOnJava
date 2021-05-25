package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimetableElement implements JsonSerializable {
    private LocalDateTime arrivingTime;
    private Ship ship;

    public TimetableElement(LocalDateTime arrivingTime, Ship ship) {
        this.arrivingTime = arrivingTime;
        this.ship = ship;
    }

    public LocalDateTime getArrivingTime() {
        return arrivingTime;
    }

    public void setArrivingTime(LocalDateTime arrivingTime) {
        this.arrivingTime = arrivingTime;
    }

    @Override
    public String toString() {
        return ship + " arrives at " + arrivingTime.format(DateTimeFormatter.ofPattern("dd:HH:mm"));
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("arrivingTime", arrivingTime.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        jsonObject.put("ship", ship.getJsonObject());
        return jsonObject;
    }

    public static TimetableElement getRandomObject(LocalDateTime currentDate, int offset) {
        TimetableElement timetableElement = new TimetableElement(currentDate, Ship.getRandomObject());
        timetableElement.setArrivingTime(LocalDateTime.of(timetableElement.getArrivingTime().getYear(),
                timetableElement.getArrivingTime().getMonth(), timetableElement.getArrivingTime().getDayOfMonth() + offset,
                timetableElement.getArrivingTime().getHour() + (int) (Math.random() * 24), timetableElement.getArrivingTime().getMinute() +
                        (int) (Math.random() * 60), timetableElement.getArrivingTime().getSecond()));
        return timetableElement;
    }
}
