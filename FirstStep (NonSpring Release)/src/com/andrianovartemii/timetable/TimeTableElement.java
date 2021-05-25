package com.andrianovartemii.timetable;

import com.andrianovartemii.tools.Utils;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeTableElement implements Comparable<TimeTableElement> {

    public GregorianCalendar arriving;
    public GregorianCalendar departure;

    protected Ship ship;

    public Ship getShip() {
        return ship;
    }


    public int compareTo(TimeTableElement el) {
        return this.departure.compareTo(el.departure);
    }

    public TimeTableElement(GregorianCalendar arriving, Ship ship) {
        this.arriving = arriving;
        this.ship = ship;
    }

    public TimeTableElement(GregorianCalendar arriving, Ship ship, GregorianCalendar departure) {
        this.arriving = arriving;
        this.ship = ship;
        this.departure = departure;
    }

    public TimeTableElement(JSONObject object) {
        this.arriving = (GregorianCalendar) parseCalendar(object.get("arriving"));
        this.departure = (GregorianCalendar) parseCalendar(object.get("departure"));
        this.ship = new Ship((JSONObject) object.get("ship"));
    }

    public static Calendar parseCalendar(Object object) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd-M-yyyy hh:mm:ss");
        try {
            calendar.setTime(format.parse((String) object));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    @Override
    public String toString() {
        String dateFormat = "dd-M-yyyy hh:mm:ss";
        return "arriving= " + new SimpleDateFormat(dateFormat, new Locale("en_US")).format(arriving.getTime()) +
                ", departure= " + new SimpleDateFormat(dateFormat, new Locale("en_US")).format(departure.getTime()) +
                ", ship= " + ship +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("arriving", Utils.CalendarToJSONObject(arriving));
        object.put("departure", Utils.CalendarToJSONObject(departure));
        object.put("ship", ship.toJSONObject());
        return object;
    }

}
