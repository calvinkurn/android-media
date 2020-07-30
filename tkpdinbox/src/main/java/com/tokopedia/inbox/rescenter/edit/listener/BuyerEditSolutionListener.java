package com.tokopedia.inbox.rescenter.edit.listener;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditSolutionListener {

    void showLoading(boolean isVisible);

    void showErrorMessage(String errorMessage);

    void finish();
}
