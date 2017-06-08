package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import android.util.Log;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadVideoModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 6/2/17.
 */

public class UploadVideoMapper implements Func1<Response<TkpdResponse>, UploadImageModel> {
    public UploadVideoMapper() {
    }

    @Override
    public UploadImageModel call(Response<TkpdResponse> response) {
        Log.d("hangnadi", "call: " + response);
        return null;
    }
}
