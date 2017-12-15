package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class SendReviewSubmitUseCase extends UseCase<SendReviewSubmitDomain> {

    protected ReputationRepository reputationRepository;
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";

    public SendReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendReviewSubmitDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.sendReviewSubmit(requestParams);
    }

    public static RequestParams getParam(SendReviewRequestModel sendReviewRequestModel) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_POST_KEY, sendReviewRequestModel.getPostKey());
        params.putString(PARAM_FILE_UPLOADED, getFileUploaded(sendReviewRequestModel));
        return params;
    }

    private static String getFileUploaded(SendReviewRequestModel sendReviewRequestModel) {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : sendReviewRequestModel.getListUpload()) {
                if (!TextUtils.isEmpty(image.getPicObj())){
                    reviewPhotos.put(image.getImageId(), image.getPicObj());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }
}
