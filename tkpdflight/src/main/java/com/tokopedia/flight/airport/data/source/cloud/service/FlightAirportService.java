package com.tokopedia.flight.airport.data.source.cloud.service;

import com.tokopedia.abstraction.common.network.services.BaseService;
import com.tokopedia.flight.airport.data.source.cloud.api.FlightAirportApi;
import com.tokopedia.flight.common.constant.FlightUrl;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class FlightAirportService extends BaseService<FlightAirportApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(FlightAirportApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return FlightUrl.BASE_URL;
    }

    @Override
    public FlightAirportApi getApi() {
        return api;
    }
}
