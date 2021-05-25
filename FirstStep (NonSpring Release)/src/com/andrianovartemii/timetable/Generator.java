package com.andrianovartemii.timetable;

import com.andrianovartemii.tools.Randomizer;
import com.andrianovartemii.tools.Utils;

import java.util.GregorianCalendar;

public class Generator {

    public static Timetable GenerateTimetable(int countOfShipsInDay, GregorianCalendar startedDate) {
        Timetable timetable = new Timetable();
        for (var dateOffset = 0; dateOffset < Integer.parseInt(Utils.readProperties().getProperty("SimulationTime")); dateOffset++) {
            for (var iter = 0; iter < (-1 + (int) (Math.random() * countOfShipsInDay)); iter++) {
                timetable.getTimetable().add(new TimeTableElement(Randomizer.GenerateDate(startedDate, dateOffset), Randomizer.GenerateShip()));
            }
        }
        return timetable;
    }
}
