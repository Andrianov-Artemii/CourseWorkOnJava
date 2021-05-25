package com.andrianovartemii.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Utils {

    public static Properties readProperties() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    public static String CalendarToJSONObject(Calendar calendar) {
        String dateFormat = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setCalendar(calendar);
        return sdf.format(calendar.getTime());
    }

}
