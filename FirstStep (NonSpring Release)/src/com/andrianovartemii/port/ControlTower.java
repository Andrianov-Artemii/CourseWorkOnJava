package com.andrianovartemii.port;

import com.andrianovartemii.timetable.Ship;
import com.andrianovartemii.tools.Log;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.logging.Level;

public class ControlTower {

    public static synchronized Ship ReturnShip(LinkedList<PortTimeTableElement> timetable, Crane crane) {
        TimetableHandler handler = TimetableHandler.onInit();
        for (PortTimeTableElement el : timetable)
            if (!el.isComplited && el.element.getShip().cargo.getType().equals(crane.type) && TimetableHandler.isElementIsArrived(el, handler.currentSimulationTime)) {
                if (el.addCrane(crane) == true) {
                    if (el.startOfUnload == null)
                        el.startOfUnload = handler.currentSimulationTime.getTime();
                    return el.element.getShip();
                }
            }
        return null;
    }

    public static synchronized void CompliteTask(LinkedList<PortTimeTableElement> timetable, Ship ship) {
        String dateFormat = "dd-M-yyyy hh:mm:ss";
        for (PortTimeTableElement el : timetable) {
            if (el.element.getShip().equals(ship)) {
                Log.out(Level.INFO, TimetableHandler.onInit().currentSimulationTime, el.element.getShip().getName() + " arrived at " + new SimpleDateFormat(dateFormat).format(el.startOfUnload) + " and was unload successfully!");
                el.endOfUnload = TimetableHandler.onInit().currentSimulationTime.getTime();
                el.isComplited = true;
                return;
            }
        }
        Log.out(Level.WARNING, TimetableHandler.onInit().currentSimulationTime, ship.getName() + " not instance in timetable!");
    }

}
