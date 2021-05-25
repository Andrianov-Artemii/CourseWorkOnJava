package com.andrianovartemii.jsonparser;

import com.andrianovartemii.timetable.TimeTableElement;
import com.andrianovartemii.timetable.Timetable;
import com.andrianovartemii.tools.Utils;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenerateJSON {

    public Boolean Generate(Timetable timetable, Calendar startSimulationTime, String fileName) {
        try {
            JSONObject object = new JSONObject();
            object.put("startSimulationTime", Utils.CalendarToJSONObject(startSimulationTime));
            object.put("timetable", timetable.toJSONObject());

            FileWriter file = new FileWriter(fileName);
            file.write(object.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
}
