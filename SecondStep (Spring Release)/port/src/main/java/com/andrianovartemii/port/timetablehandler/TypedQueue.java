package com.andrianovartemii.port.timetablehandler;

import java.sql.Time;
import java.time.Duration;
import java.util.LinkedList;

public class TypedQueue {
    private CargoType queueType;
    private LinkedList<TimetableElement> queue = new LinkedList<TimetableElement>();

    public TypedQueue(CargoType queueType) {
        this.queueType = queueType;
    }

    public void add(TimetableElement element) {
        if (queueType != element.getCargoType()) throw new RuntimeException("Invalid element type");
        boolean status = element.getArrivingTime().isBefore(TimetableHandler.getHandler().startSimulationTime) ||
                element.getArrivingTime().isAfter(TimetableHandler.getHandler().getEndSimulationTime());
        if (!status) {
            queue.add(element);
        }
    }

    public int size() {
        return queue.size();
    }

    public TimetableElement getTimetableElement(Crane crane) {
        if (queueType != crane.getCargoType()) throw new RuntimeException("Invalid element type");
        for (var element : queue) {
            if (TimetableHandler.getHandler().currentSimulationTime.isAfter(element.getArrivingTime()) && !element.isCompleted() && element.addCrane(crane))
                return element;
        }
        return null;
    }

    public TimetableElement getTimetableElementById(int id) {
        return queue.get(id);
    }

    public static int getCompletedShips(TypedQueue queue) {
        int count = 0;
        for (int i = 0; i < queue.size(); i++)
            if (queue.getTimetableElementById(i).isCompleted()) {
                count++;
            }
        return count;
    }

    public static int getSumDuration(TypedQueue queue) {
        int duration = 0;
        for (int i = 0; i < queue.size(); i++)
            duration += queue.getTimetableElementById(i).getAwaiting();
        return duration;
    }

    public static int getMaxDuration(TypedQueue queue) {
        int duration = 0;
        for (int i = 0; i < queue.size(); i++)
            duration = (queue.getTimetableElementById(i).getAwaiting() > duration) ? queue.getTimetableElementById(i).getAwaiting() : duration;
        return duration;
    }

    public static int getSumQueueAwaiting(TypedQueue queue) {
        int awaiting = 0;
        TimetableHandler handler = TimetableHandler.getHandler();
        for (int i = 0; i < queue.size(); i++) {
            var element = queue.getTimetableElementById(i);
            if (element.getStartOfUnload() != null) {
                awaiting += Duration.between(element.getArrivingTime(), element.getStartOfUnload()).getSeconds() / 60;
            } else {
                awaiting += Duration.between(element.getArrivingTime(), handler.getEndSimulationTime()).getSeconds() / 60;
            }
        }
        return awaiting;
    }

    public static LinkedList<TimetableElement> getCompletedElements(TypedQueue queue)
    {
        LinkedList<TimetableElement> elements = new LinkedList<>();
        for (int i = 0; i < queue.size(); i++) {
            var element = queue.getTimetableElementById(i);
            if(element.isCompleted() == true)
                elements.add(element);
         }
        return elements;
    }

}
