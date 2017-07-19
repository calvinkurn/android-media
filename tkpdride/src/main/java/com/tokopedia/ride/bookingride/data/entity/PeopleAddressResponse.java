package com.tokopedia.ride.bookingride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressResponse {
    @SerializedName("paging")
    @Expose
    private PeopleAddressPagingEntity paging;
    @SerializedName("list")
    @Expose
    private List<PeopleAddressEntity> list = new ArrayList<>();

    public PeopleAddressPagingEntity getPaging() {
        return paging;
    }

    public List<PeopleAddressEntity> getList() {
        return list;
    }
}
