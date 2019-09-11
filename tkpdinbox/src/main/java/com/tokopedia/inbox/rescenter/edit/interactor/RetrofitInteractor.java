package com.tokopedia.inbox.rescenter.edit.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import java.util.List;
import java.util.Map;

/**
 * Created on 8/25/16.
 */
public interface RetrofitInteractor {


    void postEditResolution(@NonNull Context context,
                            @NonNull ActionParameterPassData passData,
                            @NonNull ResultEditResolutionListener listener);

    void postEditSellerResolution(@NonNull Context context,
                                  @NonNull ActionParameterPassData passData,
                                  @NonNull ResultEditResolutionListener listener);

    void unsubscribe();

    void postAppealResolution(@NonNull Context context,
                              @NonNull ActionParameterPassData passData,
                              @NonNull ResultEditResolutionListener listener);

    interface GetEditResolutionFormListener {

        void onStart();

        void onSuccess(EditResCenterFormData formData);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

        void onFailAuth();
    }

    void getEditResolutionForm(@NonNull Context context,
                               @NonNull Map<String, String> params,
                               @NonNull GetEditResolutionFormListener listener);

    interface FormSolutionListener {

        void onSuccess(List<EditResCenterFormData.SolutionData> solutionDataList);

        void onTimeout(NetworkErrorHelper.RetryClickedListener clickedListener);

        void onError(String error);

        void onNullData();
    }

    void getSolution(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull FormSolutionListener listener);

    interface ResultEditResolutionListener {

        void onStart();

        void onSuccess();

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

        void onFailAuth();
    }

    void getAppealResolutionForm(@NonNull Context context,
                               @NonNull Map<String, String> params,
                               @NonNull GetAppealResolutionFormListener listener);

    interface GetAppealResolutionFormListener {

        void onStart();

        void onSuccess(AppealResCenterFormData formData);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

        void onFailAuth();
    }
}
