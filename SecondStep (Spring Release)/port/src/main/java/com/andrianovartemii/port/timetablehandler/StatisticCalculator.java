package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;

public class StatisticCalculator {

    LinkedList<TimetableElement> finishedElements = new LinkedList<>();
    HashMap<CargoType, Integer> fines = new HashMap<CargoType, Integer>();
    HashMap<CargoType, Integer> craneCountForAdding = new HashMap<CargoType, Integer>();
    int allShipsCount, completedShipsCount, maxDuration, averageDuration, averageAwaiting;

    public StatisticCalculator(Timetable timetable) {
        this.finishedElements.addAll(TypedQueue.getCompletedElements(timetable.getQueue(CargoType.LiquidCargo)));
        this.finishedElements.addAll(TypedQueue.getCompletedElements(timetable.getQueue(CargoType.BulkCargo)));
        this.finishedElements.addAll(TypedQueue.getCompletedElements(timetable.getQueue(CargoType.Containers)));
        this.fines.put(CargoType.LiquidCargo, calculateFinesInQueue(timetable.getQueue(CargoType.LiquidCargo)));
        this.fines.put(CargoType.BulkCargo, calculateFinesInQueue(timetable.getQueue(CargoType.BulkCargo)));
        this.fines.put(CargoType.Containers, calculateFinesInQueue(timetable.getQueue(CargoType.Containers)));
        this.allShipsCount = timetable.getSize();
        this.completedShipsCount = TypedQueue.getCompletedShips(timetable.getQueue(CargoType.LiquidCargo)) + TypedQueue.getCompletedShips(timetable.getQueue(CargoType.BulkCargo)) + TypedQueue.getCompletedShips(timetable.getQueue(CargoType.Containers));
        this.maxDuration = Math.max(Math.max(TypedQueue.getMaxDuration(timetable.getQueue(CargoType.LiquidCargo)), TypedQueue.getMaxDuration(timetable.getQueue(CargoType.BulkCargo))), TypedQueue.getMaxDuration(timetable.getQueue(CargoType.LiquidCargo)));
        this.averageDuration = calculateAverageDuration(timetable);
        this.averageAwaiting = calculateAverageQueueAwaiting(timetable);
        this.craneCountForAdding.put(CargoType.LiquidCargo, calculateCranesCount(fines.get(CargoType.LiquidCargo)));
        this.craneCountForAdding.put(CargoType.BulkCargo, calculateCranesCount(fines.get(CargoType.BulkCargo)));
        this.craneCountForAdding.put(CargoType.Containers, calculateCranesCount(fines.get(CargoType.Containers)));
    }

    public static void send(String url, StatisticCalculator statisticCalculator) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(statisticCalculator.getJSONStatistic(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
    }

    public JSONObject getJSONStatistic()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("finishedElements", Utils.getJsonArray(finishedElements));
        jsonObject.put("fines", fines);
        jsonObject.put("craneCountForAdding", craneCountForAdding);
        jsonObject.put("allShipsCount", allShipsCount);
        jsonObject.put("completedShipsCount", completedShipsCount);
        jsonObject.put("maxDuration", maxDuration);
        jsonObject.put("averageDuration", averageDuration);
        jsonObject.put("averageAwaiting", averageAwaiting);
        return jsonObject;
    }

    private int calculateAverageDuration(Timetable timetable) {
        int duration = TypedQueue.getSumDuration(timetable.getQueue(CargoType.LiquidCargo)) +
                TypedQueue.getMaxDuration(timetable.getQueue(CargoType.BulkCargo)) +
                TypedQueue.getMaxDuration(timetable.getQueue(CargoType.Containers));
        return duration / (timetable.getQueue(CargoType.LiquidCargo).size() + timetable.getQueue(CargoType.BulkCargo).size() + timetable.getQueue(CargoType.Containers).size());
    }

    private int calculateAverageQueueAwaiting(Timetable timetable) {
        int awaiting = TypedQueue.getSumQueueAwaiting(timetable.getQueue(CargoType.LiquidCargo)) +
                TypedQueue.getSumQueueAwaiting(timetable.getQueue(CargoType.BulkCargo)) +
                TypedQueue.getSumQueueAwaiting(timetable.getQueue(CargoType.Containers));
        return awaiting / (timetable.getQueue(CargoType.LiquidCargo).size() + timetable.getQueue(CargoType.BulkCargo).size() + timetable.getQueue(CargoType.Containers).size());
    }


    private int calculateFinesInQueue(TypedQueue queue) {
        TimetableHandler handler = TimetableHandler.getHandler();
        int fine = 0;
        for (int i = 0; i < queue.size(); i++) {
            var element = queue.getTimetableElementById(i);
            if (element.isCompleted()) {
                Duration duration;
                if (element.getEndOfUnload() == null) {
                    duration = Duration.between(element.getDepartureTime(), handler.getEndSimulationTime());
                } else {
                    duration = Duration.between(element.getDepartureTime(), element.getEndOfUnload());
                }
                if (duration.getSeconds() > 0)
                    fine += duration.getSeconds() / 60 * 100;
            }
        }
        return fine;
    }

    private int calculateCranesCount(Integer fines) {
        return (int) Math.floor(fines / 30000);
    }


}
