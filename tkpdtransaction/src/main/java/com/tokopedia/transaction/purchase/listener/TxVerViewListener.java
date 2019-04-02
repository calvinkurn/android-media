package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 24/05/2016.
 */
public interface TxVerViewListener extends IBaseView {

    void renderDataList(List<TxVerData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showCancelTransactionDialog(String message, String paymentId);

    void showNoConnectionLoadMoreData(String message);

    void showNoConnectionPullRefresh(String message);

    void showNoConnectionResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void showSnackbarWithMessage(String message);

    void resetData();
}
