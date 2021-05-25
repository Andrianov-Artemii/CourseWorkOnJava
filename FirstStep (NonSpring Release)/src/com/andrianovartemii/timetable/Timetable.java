package com.andrianovartemii.timetable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.LinkedList;

public class Timetable {

    protected LinkedList<TimeTableElement> timetable;

    public Timetable() {
        timetable = new LinkedList<>();
    }

    public Timetable(JSONArray array) {
        timetable = new LinkedList<>();
        for (Object el : array) {
            System.out.println("element: " + el);
            timetable.add(new TimeTableElement((JSONObject) el));
        }
    }

    public void departureSort() {
        Collections.sort(timetable);
    }

    public void add(TimeTableElement el) {
        timetable.add(el);
    }

    public LinkedList<TimeTableElement> getTimetable() {
        return timetable;
    }

    public JSONArray toJSONObject() {
        JSONArray object = new JSONArray();
        for (var el : timetable)
            object.add(el.toJSONObject());
        return object;
    }

    @Override
    public String toString() {
        String str = "";
        for (Object el : timetable) {
            str += el.toString() + '\n';
        }
        return str;
    }
}
