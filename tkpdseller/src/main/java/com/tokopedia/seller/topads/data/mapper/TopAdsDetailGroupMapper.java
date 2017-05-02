package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.data.model.response.DataResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsDetailGroupMapper implements Func1<Response<DataResponse<DataResponseCreateGroup>>, DataResponseCreateGroup> {
    @Override
    public DataResponseCreateGroup call(Response<DataResponse<DataResponseCreateGroup>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private DataResponseCreateGroup mappingResponse(Response<DataResponse<DataResponseCreateGroup>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            return dataResponseResponse.body().getData();
        } else {
            return null;
        }
    }
}
