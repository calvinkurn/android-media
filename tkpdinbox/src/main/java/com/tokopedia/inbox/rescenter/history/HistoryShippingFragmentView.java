package com.tokopedia.inbox.rescenter.history;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.history.view.model.HistoryAwbViewItem;

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

    void renderData();

    void setViewData(ArrayList<HistoryAwbViewItem> viewData);

    void doOnTrackingError(String messageError);

    void doOnTrackingSuccess(TrackingDialogViewModel model);

    void doOnTrackingFailed();

    void showLoadingDialog(boolean show);

    void showSnackBar(String messageError);

    void showTimeOutMessage();

    void doOnTrackingTimeOut();

}
