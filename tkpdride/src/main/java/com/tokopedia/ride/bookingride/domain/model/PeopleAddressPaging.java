package com.tokopedia.ride.bookingride.domain.model;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressPaging {
    private String nextUrl;
    private int page;

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
