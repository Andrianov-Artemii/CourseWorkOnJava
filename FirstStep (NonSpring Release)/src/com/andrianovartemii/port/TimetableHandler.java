package com.andrianovartemii.port;

import com.andrianovartemii.timetable.Ship;
import com.andrianovartemii.timetable.TimeTableElement;
import com.andrianovartemii.timetable.Timetable;
import com.andrianovartemii.tools.Log;
import com.andrianovartemii.tools.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.logging.Level;

public class TimetableHandler {

    public LinkedList<PortTimeTableElement> portTimetable;
    public GregorianCalendar currentSimulationTime;
    public boolean isSimulated = true;
    public static TimetableHandler handler;
    private int countOfSimulationIters = 0;


    private int averageQueueSize = 0;

    public synchronized void IteratedSimulation() {
        countOfSimulationIters--;
        handler.currentSimulationTime.add(Calendar.HOUR, 1);
        if (countOfSimulationIters == 0) {
            isSimulated = false;
        }
    }

    public static TimetableHandler onInit(String fileName) {
        if (handler == null) {
            handler = new TimetableHandler();
            JSONObject object = getJSONObject(fileName);
            handler.currentSimulationTime = (GregorianCalendar) TimeTableElement.parseCalendar(object.get("startSimulationTime"));
            Timetable timetable = new Timetable((JSONArray) object.get("timetable"));
            timetable.departureSort();
            handler.portTimetable = new LinkedList<PortTimeTableElement>();
            for (var el : timetable.getTimetable()) {
                handler.portTimetable.add(new PortTimeTableElement(el));
            }
            handler.countOfSimulationIters = Integer.parseInt(Utils.readProperties().getProperty("SimulationTime")) * 24;
        }

        return handler;
    }

    public static void getState() {
        var handler = TimetableHandler.onInit();
        int countOfUnloadShips = 0;
        float averageQueueTime = 0;
        float fines[] = new float[3];
        float curTimeFloat = handler.currentSimulationTime.getTime().getTime();
        for (var el : handler.portTimetable) {
            if (el.isComplited) {
                countOfUnloadShips++;
                averageQueueTime += el.endOfUnload.getTime() - el.startOfUnload.getTime();
                switch (el.element.getShip().cargo.getType()) {
                    case BulkCargo:
                        if (el.endOfUnload.getTime() - el.element.departure.getTime().getTime() > 0)
                            fines[0] += (int) ((el.endOfUnload.getTime() - el.element.departure.getTime().getTime()) / 3600000) * 100;
                        break;
                    case LiquidCargo:
                        if (el.endOfUnload.getTime() - el.element.departure.getTime().getTime() > 0)
                            fines[1] += (int) ((el.endOfUnload.getTime() - el.element.departure.getTime().getTime()) / 3600000) * 100;
                        break;
                    case Containers:
                        if (el.endOfUnload.getTime() - el.element.departure.getTime().getTime() > 0)
                            fines[2] += (int) ((el.endOfUnload.getTime() - el.element.departure.getTime().getTime()) / 3600000) * 100;
                        break;
                }
            }
        }
        System.out.println(
                "Simulation is ended (" + handler.currentSimulationTime.getTime() + ") complited!\n" +

                        "Ships unload: " + countOfUnloadShips + "/" + handler.portTimetable.size() + ":\n");
        for (var el : handler.portTimetable)
            if (el.isComplited)
                System.out.println(el.toString());

        System.out.println(
                "Average queue time: " + (int) (averageQueueTime / countOfUnloadShips / 3600000) + " hours \n" +
                        "Total BulkCargo fine is " + fines[0] + ". Recommended add cranes of this type is " + ((int) (fines[0] / 30000)) + '\n' +
                        "Total LiquidCargo fine is " + fines[1] + ". Recommended add cranes of this type is " + ((int) (fines[1] / 30000)) + '\n' +
                        "Total ContainersCargo fine is " + fines[2] + ". Recommended add cranes of this type is " + ((int) (fines[2] / 30000)) + '\n' +
                        "Total fines is " + (fines[0] + fines[1] + fines[2]) + '\n'
        );

    }

    public static TimetableHandler onInit() {
        if (handler == null) {
            handler = new TimetableHandler();
        }
        if (handler.portTimetable == null) {
            Log.out(Level.WARNING, handler.currentSimulationTime, "Timetable is not initialization (null)...");
        }
        return handler;
    }

    public static boolean isElementIsArrived(PortTimeTableElement element, Calendar currentTime) {
        return (currentTime.compareTo(element.element.arriving) >= 0) ? true : false;
    }

    private static JSONObject getJSONObject(String fileName) {
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = new JSONObject();
        try (FileReader reader = new FileReader(fileName)) {
            obj = (JSONObject) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void CalculateDepartments(Timetable timetable) {
        for (var el : timetable.getTimetable()) {
            el.departure = CalculateElementDepartment(el.arriving, el.getShip());
        }
    }

    private static GregorianCalendar CalculateElementDepartment(GregorianCalendar arrivingTime, Ship ship) {
        Calendar depTime = GregorianCalendar.getInstance();
        depTime.setTime(arrivingTime.getTime());
        int requestMinutes = 0;
        int craneTypeProductivity = Integer.parseInt(Utils.readProperties().getProperty(new String(ship.cargo.getType() + "CraneTypeProductivity")));
        requestMinutes = ship.cargo.weight / craneTypeProductivity;
        depTime.add(Calendar.MINUTE, requestMinutes);
        return (GregorianCalendar) depTime;
    }

}

