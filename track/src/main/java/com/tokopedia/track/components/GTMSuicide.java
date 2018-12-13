package com.tokopedia.track.components;

import android.content.Context;

public class GTMSuicide extends ContextAnalytics {

    public GTMSuicide(){
        super(null);
    }

    public GTMSuicide(Context context) {
        super(context);
    }

    public void sendEvent(String text){
        System.out.println(System.currentTimeMillis() +"  --->  "+text);
    }
}
