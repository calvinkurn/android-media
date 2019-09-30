package com.tokopedia.inbox.rescenter.edit.listener;

import android.content.Context;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.customview.AppealAttachmentView;
import com.tokopedia.inbox.rescenter.edit.customview.AppealSolutionView;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.AppealResCenterPresenter;

import java.util.List;

/**
 * Created on 8/31/16.
 */
public interface AppealResCenterListener {
    AppealResCenterPresenter getPresenter();

    String getResolutionID();

    Context getBaseContext();

    DetailResCenterData getDetailData();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

    void setLoading(boolean visible);

    void setMainView(boolean visible);

    void setTimeOutView(NetworkErrorHelper.RetryClickedListener rcListener);

    void setErrorView(String message);

    void renderShop(AppealResCenterFormData form);

    void renderInvoice(AppealResCenterFormData form);

    void renderSolution(AppealResCenterFormData formData);

    void showErrorMessage(String string);

    void showLoading(boolean visible);

    void showMainView(boolean visible);

    void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

    void finish();

    void setActivityResult();

    AppealSolutionView getSolutionView();

    AppealAttachmentView getAttachmenSectionView();

    List<ResCenterAttachment> getAttachmentData();
}
