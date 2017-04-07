package com.tokopedia.ride.bookingride.data;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;
import com.tokopedia.ride.common.exception.ResponseErrorException;

import org.json.JSONException;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 4/6/17.
 */

public class CloudPeopleAddressDataStore implements PeopleAddressDataStore {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";
    private PeopleAddressApi mPeopleAddressApi;

    public CloudPeopleAddressDataStore(PeopleAddressApi mPeopleAddressApi) {
        this.mPeopleAddressApi = mPeopleAddressApi;
    }


    @Override
    public Observable<PeopleAddressResponse> getPeopleAddress(Map<String, Object> param) {
        return mPeopleAddressApi.getAddress(param).map(new Func1<Response<TkpdResponse>, PeopleAddressResponse>() {
            @Override
            public PeopleAddressResponse call(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) throw new RuntimeException(
                            new ResponseErrorException(tkpdResponse.getErrorMessageJoined())
                    );
                    return tkpdResponse.convertDataObj(PeopleAddressResponse.class);
                } else {
                    throw new RuntimeException(
                            new ResponseErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                    );
                }
            }
        });
    }
}
