package com.tokopedia.tkpdtrain.search.domain;

import java.util.List;

/**
 * @author Rizky on 15/03/18.
 */

public class FilterParam {

    private long minPrice;
    private long maxPrice;
    private String[] departureTimeList;
    private List<String> trains;
    private List<String> trainClass;

    private FilterParam(Builder builder) {
        this.setMinPrice(builder.minPrice);
        this.setMaxPrice(builder.maxPrice);
        this.setTrains(builder.trains);
        this.setTrainClass(builder.trainClass);
        this.setDepartureTimeList(builder.departureTimeList);
    }

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

    public List<String> getTrains() {
        return trains;
    }

    public void setTrains(List<String> trains) {
        this.trains = trains;
    }

    public String[] getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(String[] departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<String> getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(List<String> trainClass) {
        this.trainClass = trainClass;
    }

    public static class Builder {

        private long minPrice;
        private long maxPrice;
        private String[] departureTimeList;
        private List<String> trains;
        private List<String> trainClass;

        public Builder minPrice(long minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(long maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder trains(List<String> trains) {
            this.trains = trains;
            return this;
        }

        public Builder trainClass(List<String> trainClass) {
            this.trainClass = trainClass;
            return this;
        }

        public Builder departureTimeList(String[] departureTimeList) {
            this.departureTimeList = departureTimeList;
            return this;
        }

        public FilterParam build() {
            return new FilterParam(this);
        }

    }

}
