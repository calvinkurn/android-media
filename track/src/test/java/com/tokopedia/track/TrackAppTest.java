package com.tokopedia.track;


import com.tokopedia.track.components.GTMSuicide;

import org.junit.Test;

public class TrackAppTest  {
    @Test
    public void simpleTest(){
        TrackApp trackApp = new TrackApp();
        GTMSuicide gtm = trackApp.getValue("GTM", GTMSuicide.class);

        new Thread(() ->{
            gtm.sendEvent("*saya*");
        }).run();

        new Thread(() ->{
            gtm.sendEvent("*delay*");
        }).run();
    }
}
