package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import android.util.Log;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.uploadimage.UploadVideoEntity;

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
        UploadImageModel domainData = new UploadImageModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadVideoEntity entity = response.body().convertDataObj(UploadVideoEntity.class);
                domainData.setSuccess(true);
                domainData.setUploadImageData(mappingEntityDomain(entity));
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    domainData.setSuccess(false);
                } else {
                    throw new MessageErrorException(response.body().getErrorMessageJoined());
                }
            }
            domainData.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return domainData;
    }

    private UploadImageData mappingEntityDomain(UploadVideoEntity entity) {
        UploadImageData data = new UploadImageData();
        data.setPicObj(entity.getPicObj());
        data.setPicSrc(entity.getPicSrc());
        return data;
    }

}
