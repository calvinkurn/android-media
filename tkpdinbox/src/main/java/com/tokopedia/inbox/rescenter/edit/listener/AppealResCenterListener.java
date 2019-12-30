package com.tokopedia.inbox.rescenter.edit.listener;

import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.AppealResCenterPresenter;

/**
 * Created on 8/31/16.
 */
public interface AppealResCenterListener {
    AppealResCenterPresenter getPresenter();

    String getResolutionID();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

    void setMainView(boolean visible);

    void showErrorMessage(String string);

    void showLoading(boolean visible);

    void finish();
}