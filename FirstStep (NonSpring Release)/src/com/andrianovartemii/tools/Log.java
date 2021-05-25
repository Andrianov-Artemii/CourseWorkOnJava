package com.andrianovartemii.tools;

import java.io.FileInputStream;
import java.io.IOException;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void out(Level level, GregorianCalendar calendar, String message)
    {
        logger.log(level, "[" + calendar.getTime() + "] : " + message);
    }
}
