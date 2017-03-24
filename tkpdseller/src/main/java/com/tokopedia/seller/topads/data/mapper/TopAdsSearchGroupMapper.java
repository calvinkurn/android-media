package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.data.model.response.DataResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSearchGroupMapper implements Func1<Response<DataResponse<List<GroupAd>>>, List<GroupAd>> {
    @Override
    public List<GroupAd> call(Response<DataResponse<List<GroupAd>>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private List<GroupAd> mappingResponse(Response<DataResponse<List<GroupAd>>> response) {
        if (response.isSuccessful() && response.body() != null
                && response.body().getData() != null) {
            return response.body().getData();
        } else {
            return Collections.emptyList();
        }
    }
}
