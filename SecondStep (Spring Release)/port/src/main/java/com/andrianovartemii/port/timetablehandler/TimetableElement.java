package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class TimetableElement {
    private LocalDateTime arrivingTime;
    private int awaiting;
    private LocalDateTime departureTime;
    private String shipName;
    private CargoType cargoType;
    private int cargoWeight;
    private LinkedList<Crane> cranes = new LinkedList<Crane>();
    private boolean isCompleted = false;

    private LocalDateTime startOfUnload;
    private LocalDateTime endOfUnload;

    public LocalDateTime getStartOfUnload() {
        return startOfUnload;
    }

    public LocalDateTime getEndOfUnload() {
        return endOfUnload;
    }

    public TimetableElement(LocalDateTime arrivingTime, String shipName, CargoType cargoType, int cargoWeight) {
        this.arrivingTime = arrivingTime;
        this.shipName = shipName;
        this.cargoType = cargoType;
        this.cargoWeight = cargoWeight;
        this.departureTime = TimetableElement.calculateDeparture(this.arrivingTime, this.cargoWeight, this.cargoType);
        this.awaiting = 0;
    }

    public void setArrivingTime(LocalDateTime arrivingTime) {
        this.arrivingTime = arrivingTime;
    }

    public LocalDateTime getArrivingTime() {
        return arrivingTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public int getAwaiting() {
        return awaiting;
    }

    public void setAwaiting(int awaiting) {
        this.awaiting = awaiting;
    }

    public static LocalDateTime calculateDeparture(LocalDateTime arrivingTime, int cargoWeight, CargoType cargoType) {
        var departureTime = LocalDateTime.of(arrivingTime.getYear(), arrivingTime.getMonth(), arrivingTime.getDayOfMonth(),
                arrivingTime.getHour(), arrivingTime.getMinute(), arrivingTime.getSecond());
        int countOfMinutes = cargoWeight / Integer.parseInt(Utils.readProperties().get(cargoType.toString() + "CraneTypeProductivity").toString());
        ;
        return departureTime.plusMinutes(countOfMinutes);
    }

    public synchronized boolean addCrane(Crane crane) {
        if (cranes.size() < 2) {
            if (cranes.size() == 0)
                startOfUnload = TimetableHandler.getHandler().currentSimulationTime;
            cranes.add(crane);
            return true;
        }
        return false;
    }

    public synchronized int unload(int weight) {
        this.cargoWeight -= weight;
        return this.cargoWeight;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {
        isCompleted = true;
        endOfUnload = TimetableHandler.getHandler().currentSimulationTime;
    }

    public LinkedList<Crane> getCranes() {
        return cranes;
    }

    @Override
    public String toString() {
        return shipName + "( " + cargoType + " " + cargoWeight + ")  arrives at " + arrivingTime.format(DateTimeFormatter.ofPattern("dd:HH:mm")) +
                " and dearture at " + departureTime.format(DateTimeFormatter.ofPattern("dd:HH:mm")) + " with awaiting equal " + awaiting;
    }

    public JSONObject getJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shipName", shipName);
        jsonObject.put("cargoType", cargoType);
        jsonObject.put("arrivingTime", arrivingTime.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        jsonObject.put("departureTime", departureTime.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        jsonObject.put("startOfUnload", startOfUnload.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        jsonObject.put("endOfUnload", endOfUnload.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        return jsonObject;
    }

    public boolean isReadyForUnload() {
        return TimetableHandler.getHandler().currentSimulationTime.isAfter(startOfUnload.plusMinutes(awaiting - 1));
    }

}
