package com.davidauz.blkm_interface.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtil {

    public static Timestamp now() {
        return new Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
