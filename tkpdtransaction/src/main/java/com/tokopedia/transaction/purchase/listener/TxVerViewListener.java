package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

import java.util.List;

/**
 * TxVerViewListener
 * Created by Angga.Prasetiyo on 24/05/2016.
 */
public interface TxVerViewListener extends ViewListener {

    void renderDataList(List<TxVerData> orderDataList, boolean hasNext, int typeRequest);

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
