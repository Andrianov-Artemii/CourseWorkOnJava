package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONArray;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Timetable {
    private LinkedList<TimetableElement> timetableElements;

    public Timetable() {
        timetableElements = new LinkedList<>();
    }

    public void add(TimetableElement element) {
        timetableElements.add(element);
    }

    public JSONArray getJsonObject() {
        JSONArray jsonArray = new JSONArray();
        for (var element : timetableElements) {
            jsonArray.add(element.getJsonObject());
        }
        return jsonArray;
    }

    public static Timetable GenerateRandomTimetable(LocalDateTime startDate, int countOfShipsInDay, int countOfDaySimulation) {
        Timetable timetable = new Timetable();
        for (int dayOffset = 0; dayOffset < countOfDaySimulation; dayOffset++) {
            for (int shipsCount = 0; shipsCount < countOfShipsInDay; shipsCount++) {
                var timetableElement = TimetableElement.getRandomObject(startDate, dayOffset);
                timetable.add(timetableElement);
            }
        }
        return timetable;
    }
}
