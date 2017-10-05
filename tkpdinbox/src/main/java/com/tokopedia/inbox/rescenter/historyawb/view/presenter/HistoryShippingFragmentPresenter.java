package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface HistoryShippingFragmentPresenter {
    void onFirstTimeLaunch();

    void doActionTrack(String shippingRefNumber, String shipmentID);

    void refreshPage();

    void setOnDestroyView();
}
