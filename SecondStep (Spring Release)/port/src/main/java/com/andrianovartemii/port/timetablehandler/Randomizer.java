package com.andrianovartemii.port.timetablehandler;

import java.time.LocalDateTime;

public class Randomizer {

    public static LocalDateTime arriving(TimetableElement element, int min, int max) {
        return element.getArrivingTime().plusDays((int) (min + Math.random() * max));
    }

    public static int interPort(TimetableElement element, int min, int max) {
        return element.getAwaiting() + (int) (min + Math.random() * max);
    }

    public static void timetableElement(TimetableElement element) {
        element.setArrivingTime(Randomizer.arriving(element, -7, 7));
        element.setAwaiting(Randomizer.interPort(element, 0, 1440));
    }
}
