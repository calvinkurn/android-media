package com.tokopedia.discovery.search.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchResponse {

    @SerializedName("process_time")
    private double processTime;
    @SerializedName("data")
    private List<SearchData> data;

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public List<SearchData> getData() {
        return data;
    }

    public void setData(List<SearchData> data) {
        this.data = data;
    }

}