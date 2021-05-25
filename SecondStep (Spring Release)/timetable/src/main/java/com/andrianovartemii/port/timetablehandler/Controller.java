package com.andrianovartemii.port.timetablehandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/timetable")
public class Controller {
    LocalDateTime startSimulation = LocalDateTime.of(1993, 1, 1, 0, 0);
    int simulationDuration = 30;

    @GetMapping("simulationConfig")
    public String getLocalDateTime() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startSimulation", startSimulation.format(DateTimeFormatter.ofPattern("dd:HH:mm")));
        jsonObject.put("duration", simulationDuration);
        return jsonObject.toJSONString();
    }

    @GetMapping("new")
    public JSONArray getJSONTimetable() {
        return Timetable.GenerateRandomTimetable(startSimulation, 3, simulationDuration).getJsonObject();
    }

}
