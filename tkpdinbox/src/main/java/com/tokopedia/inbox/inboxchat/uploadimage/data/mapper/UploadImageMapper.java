package com.tokopedia.inbox.inboxchat.uploadimage.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.uploadimage.data.pojo.UploadImagePojo;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/5/17.
 */

public class UploadImageMapper implements Func1<Response<TkpdResponse>, UploadImageDomain> {

    @Inject
    public UploadImageMapper() {
    }

    @Override
    public UploadImageDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadImagePojo pojo = response.body().convertDataObj(UploadImagePojo.class);
                return mappingToDomain(pojo);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    throw new MessageErrorException(MainApplication.getAppContext().getString(R
                            .string.default_request_error_unknown));
                } else {
                    throw new MessageErrorException(response.body().getErrorMessageJoined());
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private UploadImageDomain mappingToDomain(UploadImagePojo pojo) {
        return new UploadImageDomain(pojo.getMessageStatus(),
                pojo.getPicObj(),
                pojo.getPicSrc(),
                pojo.getServerId(),
                pojo.getSuccess());
    }
}
