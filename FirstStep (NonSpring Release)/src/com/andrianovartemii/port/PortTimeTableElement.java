package com.andrianovartemii.port;

import com.andrianovartemii.timetable.TimeTableElement;

import java.util.Date;
import java.util.LinkedList;

public class PortTimeTableElement {

    protected TimeTableElement element;
    protected LinkedList<Crane> currentCranes;

    public Date startOfUnload;
    public Date endOfUnload;
    public Boolean isComplited;

    public LinkedList<Crane> getCurrentCranes() {
        return currentCranes;
    }

    @Override
    public String toString() {
        return element.getShip().getName() + " (" + element.getShip().cargo.getType() + ") " +
                "Arrived at " + element.arriving.getTime() + " " +
                "(On unload " + element.arriving.getTime() + ")" +
                " Departure at " + endOfUnload.toString() + " " +
                "(Calculated Departure at " + element.departure.getTime().toString() + ")" +
                (((endOfUnload.getTime() - element.departure.getTime().getTime()) / 3600000 >= 0) ?
                        "(Fine = " + (endOfUnload.getTime() - element.departure.getTime().getTime()) / 3600000 + " hours)" :
                        "(Without fines)");
    }

    public PortTimeTableElement(TimeTableElement element) {
        this.currentCranes = new LinkedList<Crane>();
        this.element = element;
        this.isComplited = false;
    }

    public synchronized boolean addCrane(Crane crane) {
        if (currentCranes.size() < 2) {
            currentCranes.add(crane);
            return true;
        } else {
            return false;
        }
    }

}
