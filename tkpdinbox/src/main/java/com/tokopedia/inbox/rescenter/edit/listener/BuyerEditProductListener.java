package com.tokopedia.inbox.rescenter.edit.listener;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditProductListener {

    ActionParameterPassData getPassData();

    void showErrorMessage(String message);

    RecyclerView getProductRecyclerView();

    RecyclerView.Adapter getAdapter();

    void openSolutionFragment();
}
