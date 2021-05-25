package com.andrianovartemii.port.timetablehandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log {
    public static Logger logger;

    static {
        try (FileInputStream fileInputStream = new FileInputStream("resources/log.config")) {
            LogManager.getLogManager().readConfiguration(fileInputStream);
            logger = Logger.getLogger(Log.class.getName());
            logger.log(Level.INFO, "start log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void out(Level level, LocalDateTime simulation, String message) {
        logger.log(level, " [" + simulation.format(DateTimeFormatter.ofPattern("dd:HH:mm")) + "] : " + message);
    }
}
