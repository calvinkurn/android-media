package com.tokopedia.inbox.rescenter.edit.listener;

import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditResCenterPresenter;

/**
 * Created on 8/24/16.
 */
public interface BuyerEditResCenterListener {

    void setMainView(boolean visible);

    BuyerEditResCenterPresenter getPresenter();

    String getResolutionID();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

}
