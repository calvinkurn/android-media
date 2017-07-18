package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionTableUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.transaction.table.Cell;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void loadData(Date startDate, Date endDate, @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        RequestParams requestParam = GMStatGetTransactionTableUseCase.createRequestParam(startDate.getTime(), endDate.getTime());
        gmStatGetTransactionTableUseCase.execute(requestParam, new Subscriber<GetTransactionTableModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(GetTransactionTableModel getTransactionTable) {
                Log.d(TAG, getTransactionTable.toString());
                revealData(getTransactionTable);
            }
        });
    }

    protected void revealData(GetTransactionTableModel getTransactionTable) {
        if (isViewAttached()) {
            getView().onSearchLoaded(
                    convertToViewModel(getTransactionTable.getCells()),
                    getTransactionTable.getTotalCellCount()
            );
        }
    }

    private List<GMStatisticTransactionTableModel> convertToViewModel(List<Cell> datas) {
        List<GMStatisticTransactionTableModel> gmStatisticTransactionTableModels =
                new ArrayList<>();
        for (Cell data : datas) {
            GMStatisticTransactionTableModel gmStatisticTransactionTableModel
                    = new GMStatisticTransactionTableModel();
            gmStatisticTransactionTableModel.rightText = Integer.toString(data.getDeliveredAmt());
            gmStatisticTransactionTableModel.leftText = data.getProductProductName();
            gmStatisticTransactionTableModels.add(gmStatisticTransactionTableModel);
        }
        return gmStatisticTransactionTableModels;


    }
}
