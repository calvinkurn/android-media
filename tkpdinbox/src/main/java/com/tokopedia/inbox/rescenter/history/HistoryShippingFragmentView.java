package com.tokopedia.inbox.rescenter.history;

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
}
