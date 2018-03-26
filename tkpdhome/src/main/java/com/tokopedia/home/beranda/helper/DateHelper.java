package com.tokopedia.home.beranda.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by errysuprayogi on 3/23/18.
 */

public class DateHelper {

    public static Date getExpiredTime(String expiredTimeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZ");
        try {
            return format.parse(expiredTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

}
