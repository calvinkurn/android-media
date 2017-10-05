package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.createpicture.CreatePictureEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.uploadimage.UploadImageEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/3/17.
 */

public class CreatePictureMapper implements Func1<Response<TkpdResponse>, CreatePictureModel> {

    @Override
    public CreatePictureModel call(Response<TkpdResponse> response) {
        CreatePictureModel domainData = new CreatePictureModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                CreatePictureEntity entity = response.body().convertDataObj(CreatePictureEntity.class);
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

    private CreatePictureData mappingEntityDomain(CreatePictureEntity entity) {
        CreatePictureData data = new CreatePictureData();
        data.setFileUploaded(entity.getFileUploaded());
        data.setIsSuccess(entity.getIsSuccess());
        return data;
    }
}

