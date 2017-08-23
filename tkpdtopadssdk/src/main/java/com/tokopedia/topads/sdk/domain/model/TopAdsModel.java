package com.tokopedia.topads.sdk.domain.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsModel {

    private static final String KEY_HEADER = "header";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";

    private Error error;
    private Status status;
    private Header header;
    private List<Data> data = new ArrayList<>();
    private int adsPosition = 0;

    public TopAdsModel() {
    }

    public TopAdsModel(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ERROR)){
            JSONObject error = object.getJSONArray(KEY_ERROR).getJSONObject(0);
            setError(new Error(error));
        }
        if(!object.isNull(KEY_HEADER)) {
            setHeader(new Header(object.getJSONObject(KEY_HEADER)));
        }
        if(!object.isNull(KEY_STATUS)) {
            setStatus(new Status(object.getJSONObject(KEY_STATUS)));
        }
        if(!object.isNull(KEY_DATA)) {
            JSONArray dataArray = object.getJSONArray(KEY_DATA);
            for (int i = 0; i < dataArray.length(); i++) {
                data.add(new Data(dataArray.getJSONObject(i)));
            }
        }
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getAdsPosition() {
        return adsPosition;
    }

    public void setAdsPosition(int adsPosition) {
        this.adsPosition = adsPosition;
    }
}
