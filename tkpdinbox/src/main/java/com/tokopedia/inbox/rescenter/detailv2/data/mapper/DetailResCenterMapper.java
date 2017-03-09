package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.DetailResCenter;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterMapper implements Func1<Response<TkpdResponse>, DetailResCenter> {

    public DetailResCenterMapper() {
    }

    @Override
    public DetailResCenter call(Response<TkpdResponse> response) {
        return null;
    }
}
