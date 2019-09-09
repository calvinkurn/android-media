package com.tokopedia.inbox.rescenter.create.listener;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.List;

/**
 * Created on 6/16/16.
 */
public interface ChooseSolutionListener {

    void showAttachmentView(boolean isSetVisible);

    void showErrorMessage(String errorMessage);

    void setPassData(ActionParameterPassData passData);

    ActionParameterPassData collectInputData();

    void setRefundError(String errorMessage);

    void renderSolutionSpinner();

    void showLoading(boolean isVisible);

    void showMainView(boolean isVisible);

    void storeSolutionDataList(List<CreateResCenterFormData.SolutionData> solutionDataList);

    void onGetResultCreateResCenter(int resultCode, Bundle resultData);

    void showErrorMessageFull(String message);

    void showTimeOutFull(NetworkErrorHelper.RetryClickedListener clickedListener);

    void successFinish();

    List<ResCenterAttachment> getAttachmentData();

    Context getActivity();
}
