package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.uploadimage.UploadImageEntity;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 7/12/17.
 */

public class UploadImageV2Mapper implements Func1<Response<TkpdResponse>, UploadImageModel> {

    public UploadImageV2Mapper() {
    }

    @Override
    public UploadImageModel call(Response<TkpdResponse> response) {
        UploadImageModel domainData = new UploadImageModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadImageEntity entity = response.body().convertDataObj(UploadImageEntity.class);
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

    private UploadImageData mappingEntityDomain(UploadImageEntity entity) {
        UploadImageData data = new UploadImageData();
        data.setPicObj(entity.getPicObj());
        data.setPicSrc(entity.getPicSrc());
        return data;
    }
}
