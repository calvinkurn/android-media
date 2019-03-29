package com.tokopedia.tkpdpdp.util;

import java.util.Date;

public class ServerTimeOffsetUtil {
    public static long getServerTimeOffset(long serverTimeMillisecond){
        Date localDate = new Date();
        long localTimeMillis = localDate.getTime();
        return serverTimeMillisecond-localTimeMillis;
    }
}
