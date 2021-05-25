package com.andrianovartemii.port.timetablehandler;

import java.util.*;

public class Timetable {

    private HashMap<CargoType, TypedQueue> queues = new HashMap<>();

    public Timetable() {
        queues.put(CargoType.BulkCargo, new TypedQueue(CargoType.BulkCargo));
        queues.put(CargoType.LiquidCargo, new TypedQueue(CargoType.LiquidCargo));
        queues.put(CargoType.Containers, new TypedQueue(CargoType.Containers));
    }

    public void addToTimetable(TimetableElement element) {
        getQueue(element.getCargoType()).add(element);
    }

    public TypedQueue getQueue(CargoType cargoType) {
        return queues.get(cargoType);
    }

    public int getSize() {
        return getQueue(CargoType.LiquidCargo).size() + getQueue(CargoType.BulkCargo).size() + getQueue(CargoType.Containers).size();
    }
}
