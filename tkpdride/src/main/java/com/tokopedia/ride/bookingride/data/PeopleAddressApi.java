package com.tokopedia.ride.bookingride.data;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 4/6/17.
 */

public interface PeopleAddressApi {

    @GET("v4/people/" + TkpdBaseURL.User.PATH_GET_ADDRESS)
    Observable<Response<TkpdResponse>> getAddress(@QueryMap Map<String, Object> params);
}
