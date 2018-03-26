package com.tokopedia.tkpdtrain.search.domain;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public class FilterSearchData {

    private long minPrice;
    private long maxPrice;
    private String[] departureTimeList;
    private List<String> trains;
    private List<String> trainClass;

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String[] getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(String[] departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<String> getTrains() {
        return trains;
    }

    public void setTrains(List<String> trains) {
        this.trains = trains;
    }

    public List<String> getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(List<String> trainClass) {
        this.trainClass = trainClass;
    }
}
