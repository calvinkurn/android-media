package com.tokopedia.inbox.rescenter.historyaddress.view.presenter;

import com.tokopedia.inbox.rescenter.historyaddress.view.model.HistoryAddressViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface HistoryAddressFragmentView {
    void setResolutionID(String resolutionID);

    void setLoadingView(boolean param);

    String getResolutionID();

    void setResolutionStatus(int resolutionStatus);

    int getResolutionStatus();

    void setErrorMessage(String messageError);

    void onGetHistoryAwbFailed(String messageError);

    void onGetHistoryAwbTimeOut();

    List<HistoryAddressViewItem> getViewData();

    void renderData();

    void setViewData(ArrayList<HistoryAddressViewItem> viewData);

    void showLoadingDialog(boolean show);

    void showSnackBar(String messageError);

    void showTimeOutMessage();

}
