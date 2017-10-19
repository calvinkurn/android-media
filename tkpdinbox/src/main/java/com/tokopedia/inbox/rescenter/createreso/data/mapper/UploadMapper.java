package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import android.content.Context;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.UploadResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class UploadMapper implements Func1<Response<TkpdResponse>, UploadDomain> {

    private Context context;

    public UploadMapper(Context context) {
        this.context = context;
    }

    @Override
    public UploadDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadDomain mappingResponse(Response<TkpdResponse> response) {
        UploadResponse uploadResponse = response.body().convertDataObj(UploadResponse.class);
        UploadDomain model = new UploadDomain(uploadResponse.getPicObj(),
                uploadResponse.getPicSrc(), false);
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(context.getResources().getString(R.string.string_general_error));
                }
            } else {
                model.setSuccess(true);
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
