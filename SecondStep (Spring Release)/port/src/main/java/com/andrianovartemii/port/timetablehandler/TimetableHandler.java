package com.andrianovartemii.port.timetablehandler;

import java.time.LocalDateTime;
import java.util.logging.Level;

public class TimetableHandler {

    public LocalDateTime startSimulationTime;
    public LocalDateTime currentSimulationTime;
    public int simulationDuration;
    public Timetable timetable;

    private boolean isSimulated = true;

    private static TimetableHandler handler;

    public static TimetableHandler getHandler(LocalDateTime startSimulation, int simulationDuration) {
        if (handler == null) {
            handler = new TimetableHandler();
        }
        handler.simulationDuration = simulationDuration;
        handler.startSimulationTime = startSimulation;
        handler.currentSimulationTime = startSimulation;
        return handler;
    }

    public LocalDateTime getEndSimulationTime() {
        return startSimulationTime.plusDays(simulationDuration);
    }

    public void simulationStepInMinutes() {
        currentSimulationTime = currentSimulationTime.plusMinutes(1);
        if (currentSimulationTime.isEqual(getEndSimulationTime()) || currentSimulationTime.isAfter(getEndSimulationTime())) {
            isSimulated = false;
            Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, "End of simulation!");
            StatisticCalculator.send(Application.apiUrl+"8082/statistic", new StatisticCalculator(timetable));
        } else Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, "End of iteration...");
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public boolean isSimulated() {
        return isSimulated;
    }

    public static TimetableHandler getHandler() {
        if (handler == null) {
            throw new RuntimeException("handler is null!");
        }
        return handler;
    }

}
