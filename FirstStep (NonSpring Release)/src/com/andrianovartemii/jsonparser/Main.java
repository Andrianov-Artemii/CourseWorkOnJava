package com.andrianovartemii.jsonparser;

import com.andrianovartemii.port.TimetableHandler;
import com.andrianovartemii.timetable.Generator;
import com.andrianovartemii.tools.Randomizer;
import com.andrianovartemii.timetable.Timetable;
import com.andrianovartemii.tools.Utils;

import java.util.GregorianCalendar;


public class Main {
    public static void main(String[] args) {
        GregorianCalendar startSimulationTime = new GregorianCalendar(1991, 3, 1);
        Timetable timetable = Generator.GenerateTimetable(Integer.parseInt(Utils.readProperties().getProperty("CountOfShipsInDay")), startSimulationTime);
        TimetableHandler.CalculateDepartments(timetable);
        GenerateJSON generator = new GenerateJSON();
        generator.Generate(timetable, startSimulationTime, "timetable.json");
        Randomizer.RandomizeTimetable(timetable, 7, 1440);
        generator.Generate(timetable, startSimulationTime, "timetableRand.json");
    }
}
