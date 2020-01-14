package com.tokopedia.inbox.rescenter.edit.listener;

import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.SellerEditResCenterPresenter;

/**
 * Created on 8/29/16.
 */
public interface SellerEditResCenterListener {

    SellerEditResCenterPresenter getPresenter();

    String getResolutionID();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

    void setMainView(boolean visible);

    void showErrorMessage(String string);

    void showLoading(boolean visible);

    void finish();
}
