package com.tokopedia.track;


import android.content.Context;

import com.tokopedia.track.components.GTMSuicide;
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class TrackAppTest  {
    @Test
    public void simpleTest(){
        TrackApp trackApp = new TrackApp();
        GTMSuicide gtm = (GTMSuicide) trackApp.getValue("GTM");

        new Thread(() ->{
            gtm.sendEvent("*saya*");
        }).run();

        new Thread(() ->{
            gtm.sendEvent("*delay*");
        }).run();
    }

    @Test
    public void testAja(){
        TrackApp.initTrackApp(RuntimeEnvironment.application.getApplicationContext());
        TrackApp trackApp = TrackApp.getInstance();
        trackApp.registerImplementation("TEST", TestSuicide.class);

        ((TestSuicide)trackApp.getValue("TEST")).test1 = 2;

        Assert.assertEquals(2,  ((TestSuicide)trackApp.getValue("TEST")).test1);

        assert trackApp.context==RuntimeEnvironment.application.getApplicationContext();

        trackApp.initializeAllApis();
    }

    static class TestSuicide extends ContextAnalytics {
        public int test1 = 1;

        public TestSuicide(Context context) {
            super(context);
        }

        @Override
        public void initialize() {
            test1 = 1;
        }
    }
}
