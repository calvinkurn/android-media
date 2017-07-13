package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionTableUseCase;

import java.util.Date;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTablePresenterImpl extends GMStatisticTransactionTablePresenter {
    private static final String TAG = "GMStatisticTransactionT";
    GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase;

    public GMStatisticTransactionTablePresenterImpl(GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase) {
        this.gmStatGetTransactionTableUseCase = gmStatGetTransactionTableUseCase;
    }

    public void loadData(Date startDate, Date endDate) {
        RequestParams requestParam = GMStatGetTransactionTableUseCase.createRequestParam(startDate.getTime(), endDate.getTime());
        gmStatGetTransactionTableUseCase.execute(requestParam, new Subscriber<GetTransactionTable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(GetTransactionTable getTransactionTable) {
                Log.d(TAG, getTransactionTable.toString());
            }
        });
    }
}
