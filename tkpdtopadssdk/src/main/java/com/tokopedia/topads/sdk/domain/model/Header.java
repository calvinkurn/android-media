package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Header {
    private int totalData;
    private double processTime;
    private MetaData metaData;

    private static final String KEY_META_DATA = "meta";
    private static final String KEY_TOTAL_DATA = "total_data";
    private static final String KEY_PROCESS_TIME = "process_time";

    public Header(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_META_DATA)) {
            setMetaData(new MetaData(object.getJSONObject(KEY_META_DATA)));
        }
        if(!object.isNull(KEY_PROCESS_TIME)) {
            setProcessTime(object.getDouble(KEY_PROCESS_TIME));
        }
        if(!object.isNull(KEY_TOTAL_DATA)) {
            setTotalData(object.getInt(KEY_TOTAL_DATA));
        }
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
