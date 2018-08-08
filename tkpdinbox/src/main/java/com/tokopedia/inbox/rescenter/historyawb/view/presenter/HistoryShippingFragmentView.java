package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.historyawb.view.model.HistoryAwbViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface HistoryShippingFragmentView {
    void setResolutionID(String resolutionID);

    void setLoadingView(boolean param);

    String getResolutionID();

    void onActionEditClick(String conversationID, String shipmentID, String shippingRefNumber);

    void onActionTrackClick(String shipmentID, String shippingRefNumber);

    void showInpuNewShippingAwb(boolean param);

    void setErrorMessage(String messageError);

    void onGetHistoryAwbFailed(String messageError);

    void onGetHistoryAwbTimeOut();

    List<HistoryAwbViewItem> getViewData();

    void setResolutionStatus(int resolutionStatus);

    int getResolutionStatus();

    void renderData();

    void setViewData(ArrayList<HistoryAwbViewItem> viewData);

    void showLoadingDialog(boolean show);

    void showSnackBar(String messageError);

    void showTimeOutMessage();

    void resetList();
}
