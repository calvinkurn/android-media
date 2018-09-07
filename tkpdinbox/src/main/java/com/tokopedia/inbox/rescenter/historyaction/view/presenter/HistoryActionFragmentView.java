package com.tokopedia.inbox.rescenter.historyaction.view.presenter;

import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface HistoryActionFragmentView {
    void setResolutionID(String resolutionID);

    void setLoadingView(boolean param);

    String getResolutionID();

    void setErrorMessage(String messageError);

    void onGetHistoryAwbFailed(String messageError);

    void onGetHistoryAwbTimeOut();

    List<HistoryActionViewItem> getViewData();

    void renderData();

    void setViewData(ArrayList<HistoryActionViewItem> viewData);

    void showLoadingDialog(boolean show);

    void showSnackBar(String messageError);

    void showTimeOutMessage();

    void setResolutionStatus(int resolutionStatus);

    int getResolutionStatus();
}
