package com.andrianovartemii.port;

import com.andrianovartemii.timetable.*;
import com.andrianovartemii.tools.Utils;

import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        LinkedList<Thread> cranes = new LinkedList<Thread>();
        int bulkCargoCount = Integer.parseInt(Utils.readProperties().getProperty("BulkCargoCount"));
        int liquidCargoCount = Integer.parseInt(Utils.readProperties().getProperty("LiquidCargoCount"));
        int containersCount = Integer.parseInt(Utils.readProperties().getProperty("ContainersCount"));
        TimetableHandler handler = TimetableHandler.onInit("timetableRand.json");
        CyclicBarrier barrier = new CyclicBarrier(liquidCargoCount + bulkCargoCount + containersCount, new Runnable() {
            @Override
            public void run() {
                handler.IteratedSimulation();
            }
        });
        CyclicBarrier statBarrier = new CyclicBarrier(liquidCargoCount + bulkCargoCount + containersCount, new Runnable() {
            @Override
            public void run() {
                TimetableHandler.getState();
            }
        });
        for (int i = 0; i < liquidCargoCount; i++) {
            cranes.add(new Thread(new Crane(barrier, statBarrier, CargoType.LiquidCargo)));
        }
        for (int i = 0; i < bulkCargoCount; i++) {
            cranes.add(new Thread(new Crane(barrier, statBarrier, CargoType.BulkCargo)));
        }
        for (int i = 0; i < containersCount; i++) {
            cranes.add(new Thread(new Crane(barrier, statBarrier, CargoType.Containers)));
        }
        System.out.println(cranes.size());
        for (var crane : cranes)
            crane.start();
    }

}
