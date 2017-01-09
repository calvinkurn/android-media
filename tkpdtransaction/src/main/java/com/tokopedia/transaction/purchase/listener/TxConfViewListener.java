package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;

import java.util.List;

/**
 * @author by Angga.Prasetiyo on 13/05/2016.
 */
public interface TxConfViewListener extends IBaseView {
    void renderDataList(List<TxConfData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showNoConnectionLoadMoreData(String message);

    void showNoConnectionPullRefresh(String message);

    void showNoConnectionResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void resetData();

    void closeActionMode();

    void resetTxConfDataSelected();
}
