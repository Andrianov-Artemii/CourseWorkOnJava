package com.andrianovartemii.port.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;

@RestController
public class Controller implements ErrorController {
    static RestTemplate restTemplate = new RestTemplate();
    static String apiUrl = "http://localhost:";
    static int nameCount = 0;

    @GetMapping("/getTimetable")
    public void getTimetable() {
        ResponseEntity<JSONArray> timetable = restTemplate.getForEntity(apiUrl + "8081/timetable/new", JSONArray.class);
        FileWriter file = null;
        try {
            file = new FileWriter("timetable" + nameCount + ".json");
            file.write(timetable.getBody().toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nameCount++;
    }

    @GetMapping("/getTimetableAsFile/{fileName}")
    public ResponseEntity<String> startSimulation(@PathVariable(value = "fileName") String fileName) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        try (FileReader reader = new FileReader(fileName)) {
            jsonArray = (JSONArray) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not found");
        } catch (IOException | ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parse error");
        }
        return new ResponseEntity<>(jsonArray.toJSONString(), HttpStatus.OK);
    }

    static int statisticCount = 0;

    @PostMapping(value = "statistic", consumes = "application/json", produces = "application/json")
    public void postStatistic(@RequestBody JSONObject statistic) {
        FileWriter file = null;
        try {
            file = new FileWriter("statistic" + statisticCount + ".json");
            file.write(statistic.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errors on file write!");
        }
        statisticCount++;
    }

    @PostMapping(value = "addElement", consumes = "application/json", produces = "application/json")
    public void addElement(@RequestBody JSONObject statistic) {
        System.out.println(statistic);
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter((String) statistic.get("fileName"));
            fileWriter.write((((JSONObject) statistic.get("element")).toJSONString()));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errors on file write!");
        }
    }

    @Override
    @GetMapping("/error")
    public String getErrorPath() {
        return "Oops!";
    }
}
