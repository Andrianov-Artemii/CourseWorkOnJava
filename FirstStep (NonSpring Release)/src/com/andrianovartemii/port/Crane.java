package com.andrianovartemii.port;

import com.andrianovartemii.timetable.CargoType;
import com.andrianovartemii.timetable.Ship;
import com.andrianovartemii.tools.Log;
import com.andrianovartemii.tools.Utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

public class Crane implements Runnable {
    public CargoType type;
    public Ship currentShip;
    private CyclicBarrier barrier;
    private CyclicBarrier statBarrier;

    protected int productivity;

    public int getProductivity() {
        return productivity;
    }

    public Crane(CyclicBarrier barrier, CyclicBarrier statBarrier, CargoType type) {
        this.type = type;
        this.barrier = barrier;
        this.statBarrier = statBarrier;
        this.productivity = Integer.parseInt(Utils.readProperties().getProperty(type.toString() + "CraneTypeProductivity"));
    }

    @Override
    public String toString() {
        return "Crane " + Thread.currentThread().getId() + " ( " + this.type + ")";

    }

    @Override
    public void run() {
        simulation();
    }

    private synchronized void simulation() {
        TimetableHandler handler = TimetableHandler.onInit();
        while (handler.isSimulated) {
            Log.out(Level.INFO, handler.currentSimulationTime, this.toString() + ": ITERATION");
            if (unloadShip()) {
                ControlTower.CompliteTask(handler.portTimetable, currentShip);
                currentShip = null;
            }

            if (currentShip == null) {
                Log.out(Level.INFO, handler.currentSimulationTime, this.toString() + ": Wait Ship of type " + type + "...");
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        try {
            statBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        Log.out(Level.INFO, handler.currentSimulationTime, this.toString() + ": successfully completed the simulation!");

    }

    public void setShip(Ship newShip) {
        if (!newShip.cargo.getType().equals(this.type)) {
            Log.out(Level.WARNING, TimetableHandler.onInit().currentSimulationTime, "Crane type " + type + " is not equal ship type " + newShip.cargo.getType() + ".");
            currentShip = null;
        }
        else currentShip = newShip;
    }

    private synchronized boolean unloadShip() {
        var handler = TimetableHandler.onInit();
        if (currentShip == null || currentShip.cargo.weight <= 0)
            currentShip = ControlTower.ReturnShip(handler.portTimetable, this);
        if (currentShip == null) return false;
        currentShip.cargo.weight -= productivity;
        Log.out(Level.INFO, handler.currentSimulationTime, this.toString() + ": unload" + currentShip.getName() + ". Remainder = " + currentShip.cargo.weight + "(-" + productivity + ")");
        if (currentShip.cargo.weight <= 0) {

            return true;
        }
        return false;
    }

}
