package com.tokopedia.flight.search.data.cloud.model.request;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by User on 11/8/2017.
 */

public class FlightSearchSingleRequestData {
    public static final String SEARCH_SINGLE = "search_single";
    @SerializedName("type")
    private String type;
    @SerializedName("attributes")
    private Attributes attributes;

    public FlightSearchSingleRequestData(FlightSearchApiRequestModel flightSearchApiRequestModel) {
        type = SEARCH_SINGLE;
        attributes = new Attributes(flightSearchApiRequestModel);
    }

    public Attributes getAttributes() {
        return attributes;
    }

}
