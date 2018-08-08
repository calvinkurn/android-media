package com.tokopedia.inbox;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author by nisie on 9/15/17.
 */

public class TimeTickerUtilTest {

    private static final int MILLISECONDS = 1000;
    private static final long EXAMPLE_DURATION_IN_SECONDS = 141048 * MILLISECONDS;
    private static final long TEST_SECOND = 48;
    private static final long TEST_MINUTE = 10;
    private static final long TEST_HOUR = 15;
    private static final long TEST_DAY = 1;

    @Test
    public void testSecond() throws Exception {
        assertEquals(TEST_SECOND, getSecond(EXAMPLE_DURATION_IN_SECONDS));
    }

    @Test
    public void testMinute() throws Exception {
        assertEquals(TEST_MINUTE, getMinute(EXAMPLE_DURATION_IN_SECONDS));
    }

    @Test
    public void testHour() throws Exception {
        assertEquals(TEST_HOUR, getHour(EXAMPLE_DURATION_IN_SECONDS));
    }

    @Test
    public void testDay() throws Exception {
        assertEquals(TEST_DAY, getDay(EXAMPLE_DURATION_IN_SECONDS));
    }

    private long getSecond(long millisUntilFinished) {
        return TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
    }

    private long getMinute(long millisUntilFinished) {
        return (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 3600) / 60;
    }

    private long getHour(long millisUntilFinished) {
        return (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 86400) / 3600;
    }

    private long getDay(long millisUntilFinished) {
        return TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) / 86400;
    }

}
