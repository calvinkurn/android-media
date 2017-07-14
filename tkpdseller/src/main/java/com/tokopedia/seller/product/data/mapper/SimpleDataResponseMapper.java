package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.topads.dashboard.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendry on 4/21/2017.
 */

public class SimpleDataResponseMapper<T> implements Func1<Response<DataResponse<T>>, T> {
    @Inject
    public SimpleDataResponseMapper() {
    }

    @Override
    public T call(Response<DataResponse<T>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private T mappingResponse(Response<DataResponse<T>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            return dataResponseResponse.body().getData();
        } else {
            return null;
        }
    }
}
