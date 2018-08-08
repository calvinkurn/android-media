package com.tokopedia.inbox.rescenter.edit.listener;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.edit.customview.EditAttachmentView;
import com.tokopedia.inbox.rescenter.edit.customview.EditSolutionView;
import com.tokopedia.inbox.rescenter.edit.customview.MessageView;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import java.util.List;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditSolutionListener {

    EditSolutionView getSolutionSectionView();

    EditAttachmentView getAttachmenSectionView();

    MessageView getMessageView();

    void showLoading(boolean isVisible);

    void showMainView(boolean isVisible);

    void storeSolutionDataList(List<EditResCenterFormData.SolutionData> dataList);

    void renderSolutionSpinner();

    void showAttachmentView(boolean visible);

    void showTimeOutFull(NetworkErrorHelper.RetryClickedListener clickedListener);

    void showErrorMessageFull(String error);

    void showErrorMessage(String errorMessage);

    ActionParameterPassData getActionParameterPassData();

    void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

    void finish();

    void setActivityResult();
}
