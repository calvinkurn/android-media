package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoRequestDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class CreateSubmitUseCase extends UseCase<CreateSubmitDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_CACHE_KEY = "cacheKey";
    public static final String PARAM_PICTURES = "pictures";
    public static final String PARAM_VIDEOS = "videos";
    public static final String PARAM_JSON = "param_json";
    public static final String RESOLUTION_ID = "resolutionID";

    private CreateValidateSubmitRepository createValidateSubmitRepository;

    public CreateSubmitUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               CreateValidateSubmitRepository createValidateSubmitRepository) {
        super(threadExecutor, postExecutionThread);
        this.createValidateSubmitRepository = createValidateSubmitRepository;
    }

    @Override
    public Observable<CreateSubmitDomain> createObservable(RequestParams requestParams) {
        return createValidateSubmitRepository.submit(requestParams);
    }

    public static RequestParams createResoSubmitParams(CreateResoRequestDomain createResoRequestDomain) {
        JSONObject submitObject = new JSONObject();
        try {
            submitObject.put(PARAM_CACHE_KEY,
                    createResoRequestDomain.getCreateValidateDomain().getCacheKey());
            JSONArray imageArrayList = new JSONArray();
            JSONArray videoArrayList = new JSONArray();

            for (UploadDomain uploadDomain : createResoRequestDomain.getUploadDomain()) {
                if (uploadDomain.isVideo()) {
                    videoArrayList.put(uploadDomain.getPicObj());
                } else {
                    imageArrayList.put(uploadDomain.getPicObj());
                }
            }
            submitObject.put(PARAM_PICTURES, imageArrayList);
            submitObject.put(PARAM_VIDEOS, videoArrayList);
            if (!TextUtils.isEmpty(createResoRequestDomain.getResolutionId())) {
                submitObject.put(RESOLUTION_ID, Integer.valueOf(createResoRequestDomain.getResolutionId()));
            }
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, createResoRequestDomain.getOrderId());
            params.putString(PARAM_JSON, submitObject.toString());
            return params;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
