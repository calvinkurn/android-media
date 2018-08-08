package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditSolutionPresenter {

    void setOnSubmitClick(Context context);

    void onFirstTimeLaunched(Context context, ActionParameterPassData passData);

    void unsubscribe();
}
