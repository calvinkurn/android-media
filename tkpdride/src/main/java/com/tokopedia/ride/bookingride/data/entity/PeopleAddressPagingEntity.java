package com.tokopedia.ride.bookingride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressPagingEntity {
    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
    private String uriPrevious;

    /**
     *
     * @return
     *     The uriNext
     */
    public String getUriNext() {
        return uriNext;
    }

    /**
     *
     * @param uriNext
     *     The uri_next
     */
    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    /**
     *
     * @return
     *     The uriPrevious
     */
    public String getUriPrevious() {
        return uriPrevious;
    }

    /**
     *
     * @param uriPrevious
     *     The uri_previous
     */
    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }
}
