package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoRequestDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class CreateSubmitUseCase extends UseCase<CreateSubmitDomain> {
    private static final String ORDER_ID = "order_id";
    private static final String PARAM_CACHE_KEY = "cacheKey";
    private static final String PARAM_PICTURES = "pictures";
    private static final String PARAM_VIDEOS = "videos";
    private static final String PARAM_ATTACHMENT = "attachment";
    private static final String PARAM_OBJECTS = "objects";
    public static final String PARAM_JSON = "param_json";
    private static final String RESOLUTION_ID = "resolutionID";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public CreateSubmitUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<CreateSubmitDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.createSubmit(requestParams);
    }

    public static RequestParams createResoSubmitParams(CreateResoRequestDomain createResoRequestDomain) {
        JsonObject submitObject = new JsonObject();
        submitObject.addProperty(PARAM_CACHE_KEY,
                createResoRequestDomain.getCreateValidateDomain().getCacheKey());
        JsonArray imageArrayList = new JsonArray();
        JsonArray videoArrayList = new JsonArray();

        for (UploadDomain uploadDomain : createResoRequestDomain.getUploadDomain()) {
            if (uploadDomain.isVideo()) {
                videoArrayList.add(uploadDomain.getPicObj());
            } else {
                imageArrayList.add(uploadDomain.getPicObj());
            }
        }
        JsonObject attachmentObject = new JsonObject();
        JsonObject picObject = new JsonObject();
        JsonObject videoObject = new JsonObject();
        picObject.add(PARAM_OBJECTS, imageArrayList);
        videoObject.add(PARAM_OBJECTS, videoArrayList);
        attachmentObject.add(PARAM_PICTURES, picObject);
        attachmentObject.add(PARAM_VIDEOS, videoObject);
        submitObject.add(PARAM_ATTACHMENT, attachmentObject);
        if (!TextUtils.isEmpty(createResoRequestDomain.getResolutionId())) {
            submitObject.addProperty(RESOLUTION_ID, Integer.valueOf(createResoRequestDomain.getResolutionId()));
        }
        RequestParams params = RequestParams.create();
        params.putString(ORDER_ID, createResoRequestDomain.getOrderId());
        params.putObject(PARAM_JSON, submitObject);
        if (!TextUtils.isEmpty(createResoRequestDomain.getResolutionId())) {
            params.putString(RESOLUTION_ID, createResoRequestDomain.getResolutionId());
        }
        return params;
    }
}
