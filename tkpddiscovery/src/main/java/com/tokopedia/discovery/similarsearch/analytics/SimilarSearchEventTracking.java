package com.tokopedia.discovery.similarsearch.analytics;

import android.util.Log;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

import java.util.Map;

public class SimilarSearchEventTracking extends EventTracking{

    SimilarSearchEventTracking(String event, String category, String action, String label, Map<String,Object> ecommerce) {
        super( event,  category,  action,  label);
        Log.d("GAv4", "EventTracking: ecommerce " + ecommerce);
        this.eventTracking.put("ecommerce",ecommerce);
    }



}
