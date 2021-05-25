package com.andrianovartemii.tools;

import com.andrianovartemii.timetable.CargoType;
import com.andrianovartemii.timetable.Ship;
import com.andrianovartemii.timetable.ShipCargo;
import com.andrianovartemii.timetable.Timetable;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Randomizer {

    public static GregorianCalendar GenerateDate(final GregorianCalendar currentDate, int dateOffset) {
        int hourRand = (int) (Math.random() * 24);
        int minutesRand = (int) (Math.random() * 60);
        int secondsRand = (int) (Math.random() * 24);
        GregorianCalendar date = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH) + dateOffset, hourRand, minutesRand, secondsRand);
        return date;
    }

    public static int RandomizeTimetableElement(int minDateOffset, int maxDateOffset) {
        int offset = minDateOffset + (int) (Math.random() * maxDateOffset * 2);
        return offset;
    }


    public static Timetable RandomizeTimetable(Timetable timetable, int daysRange, int unloadRange) {
        for (var el : timetable.getTimetable()) {
            int offsetArriveing = Randomizer.RandomizeTimetableElement(-daysRange, daysRange);
            el.arriving.add(Calendar.DAY_OF_MONTH, offsetArriveing);
            el.departure.add(Calendar.DAY_OF_MONTH, offsetArriveing);
            el.arriving.add(Calendar.MINUTE, Randomizer.RandomizeTimetableElement(0, unloadRange));
        }
        return timetable;
    }


    public static int shipId = 0;

    public static Ship GenerateShip() {
        return new Ship("Ship [Number " + (shipId++) + "]", GenerateCargo(), 100 + (int) (Math.random() * 200));
    }

    private static ShipCargo GenerateCargo() {
        int cargoCount = (int) (Math.random() * 3);
        CargoType type;
        switch (cargoCount) {
            case 0 -> type = CargoType.BulkCargo;
            case 1 -> type = CargoType.LiquidCargo;
            default -> type = CargoType.Containers;
        }
        return new ShipCargo(type, 20 + (int) (Math.random() * 100));
    }


}
