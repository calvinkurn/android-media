package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressPagingEntity;

import java.util.List;

/**
 * Created by alvarisi on 7/19/17.
 */

public class RideHistoryResponse {
    @SerializedName("history")
    @Expose
    private List<RideHistoryEntity> histories;
    @SerializedName("paging")
    @Expose
    private PeopleAddressPagingEntity paging;

    public List<RideHistoryEntity> getHistories() {
        return histories;
    }

    public PeopleAddressPagingEntity getPaging() {
        return paging;
    }
}
