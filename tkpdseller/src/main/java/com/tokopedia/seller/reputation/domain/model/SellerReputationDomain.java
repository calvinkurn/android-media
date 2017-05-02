package com.tokopedia.seller.reputation.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 3/16/17.
 */

public class SellerReputationDomain {
    private Links links;

    private List<Data> list = new ArrayList<Data>();


    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }


    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    public static class Data {
        private String date;

        private String information;

        private float penaltyScore;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public float getPenaltyScore() {
            return penaltyScore;
        }

        public void setPenaltyScore(float penaltyScore) {
            this.penaltyScore = penaltyScore;
        }
    }


    public static class Links {
        private String self;

        private String next;

        private String prev;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }
    }
}
