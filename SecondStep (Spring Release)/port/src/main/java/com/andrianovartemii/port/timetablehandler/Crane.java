package com.andrianovartemii.port.timetablehandler;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

public class Crane implements Runnable {

    private TypedQueue queue;
    private TimetableElement currentTimetableElement;
    private CargoType cargoType;
    private int productivity;
    private CyclicBarrier cyclicBarrier;

    public Crane(CargoType cargoType, Timetable timetable, CyclicBarrier cyclicBarrier) {
        this.cargoType = cargoType;
        this.queue = timetable.getQueue(this.cargoType);
        this.cyclicBarrier = cyclicBarrier;
        this.currentTimetableElement = null;
        this.productivity = Integer.parseInt(Utils.readProperties().get(cargoType.toString() + "CraneTypeProductivity").toString());
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public void setCurrentTimetableElement(TimetableElement currentTimetableElement) {
        this.currentTimetableElement = currentTimetableElement;
    }


    public synchronized void unloadCargo() {
        if (currentTimetableElement == null || currentTimetableElement.isCompleted() == true)
            setCurrentTimetableElement(queue.getTimetableElement(this));
        if (currentTimetableElement != null) {
            if (!currentTimetableElement.isReadyForUnload())
                Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, Thread.currentThread().getName() + " wait inter-port " + currentTimetableElement.toString() );
            else if(currentTimetableElement.unload(productivity) <= 0) {
                Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, Thread.currentThread().getName() + " finish unload " + currentTimetableElement.toString());
                currentTimetableElement.complete();
            }
            else Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, Thread.currentThread().getName() + " unload " + currentTimetableElement.toString());

        }
    }


    @Override
    public synchronized void run()  {
        while (TimetableHandler.getHandler().isSimulated()) {
            try {
                unloadCargo();
                cyclicBarrier.await();
                //Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }


    }
}
