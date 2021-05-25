package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

public class Application {

    static RestTemplate restTemplate = new RestTemplate();
    static String apiUrl = "http://localhost:";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input timetable name: ");
        String fileName = in.nextLine();
        ResponseEntity<String> currentSim = restTemplate.getForEntity(apiUrl + "8081/timetable/simulationConfig", String.class);
        JSONObject config = (JSONObject) JSONValue.parse(currentSim.getBody().toString());
        TimetableHandler timetableHandler = TimetableHandler.getHandler(Utils.convertDateTime(config.get("startSimulation").toString()),
                Integer.parseInt(config.get("duration").toString()));
        ResponseEntity<JSONArray> elements = restTemplate.getForEntity(apiUrl + "8082/getTimetableAsFile/" + fileName, JSONArray.class);
        timetableHandler.setTimetable(new Timetable());
        JSONArray jsonArray = (JSONArray) JSONValue.parse(elements.getBody().toString());
        for (var el : jsonArray) {
            TimetableElement timetableElement = Utils.convertTimetableElement((JSONObject) el);
            Randomizer.timetableElement(timetableElement);
            timetableHandler.getTimetable().addToTimetable(timetableElement);
        }
        int liquidCargoCraneCount = Integer.parseInt(Utils.readProperties().get("LiquidCargoCount").toString());
        int bulkCargoCraneCount = Integer.parseInt(Utils.readProperties().get("BulkCargoCount").toString());
        int containersCraneCount = Integer.parseInt(Utils.readProperties().get("ContainersCount").toString());


        CyclicBarrier cyclicBarrier = new CyclicBarrier(liquidCargoCraneCount + bulkCargoCraneCount + containersCraneCount, new Runnable() {
            @Override
            public void run() {
                timetableHandler.simulationStepInMinutes();
            }
        });
        LinkedList<Thread> cranes = createCranes(liquidCargoCraneCount, bulkCargoCraneCount, containersCraneCount, cyclicBarrier);
        Log.out(Level.INFO, TimetableHandler.getHandler().currentSimulationTime, "Created " + cranes.size() + " cranes");

        for (var crane : cranes)
            crane.start();
    }

    private static LinkedList<Thread> createCranes(int liquidCargo, int bulkCargo, int containers, CyclicBarrier cyclicBarrier) {
        LinkedList<Thread> cranes = new LinkedList<>();
        for (int i = 0; i < liquidCargo; i++) {
            cranes.add(new Thread(new Crane(CargoType.LiquidCargo, TimetableHandler.getHandler().getTimetable(), cyclicBarrier)));
        }
        for (int i = 0; i < bulkCargo; i++) {
            cranes.add(new Thread(new Crane(CargoType.BulkCargo, TimetableHandler.getHandler().getTimetable(), cyclicBarrier)));
        }
        for (int i = 0; i < containers; i++) {
            cranes.add(new Thread(new Crane(CargoType.Containers, TimetableHandler.getHandler().getTimetable(), cyclicBarrier)));
        }
        return cranes;
    }
}