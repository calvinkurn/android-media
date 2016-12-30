package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 21/04/2016.
 */
public interface TxListViewListener extends ViewListener {
    void renderDataList(List<OrderData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showNoConnectionLoadMoreData(String message);

    void showNoConnectionPullRefresh(String message);

    void showNoConnectionResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void resetData();

}
