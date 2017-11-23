package com.tokopedia.gm.statistic.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetTransactionTableUseCase;
import com.tokopedia.gm.statistic.domain.model.transaction.table.Cell;
import com.tokopedia.gm.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.gm.statistic.view.adapter.model.GMStatisticTransactionTableModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTablePresenterImpl extends GMStatisticTransactionTablePresenter {
    private static final String TAG = "GMStatisticTransactionT";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase;

    public GMStatisticTransactionTablePresenterImpl(GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase) {
        this.gmStatGetTransactionTableUseCase = gmStatGetTransactionTableUseCase;
    }

    public void loadData(Date startDate, Date endDate, @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy final int sortBy,
                         int page) {
        RequestParams requestParam = GMStatGetTransactionTableUseCase.createRequestParam(
                startDate.getTime(),
                endDate.getTime(),
                page,
                DEFAULT_PAGE_SIZE,
                sortType,
                sortBy);
        gmStatGetTransactionTableUseCase.execute(requestParam, new Subscriber<GetTransactionTableModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onLoadSearchError(e);
                }
            }

            @Override
            public void onNext(GetTransactionTableModel getTransactionTable) {
                revealData(getTransactionTable, sortBy);
            }
        });
    }

    protected void revealData(GetTransactionTableModel getTransactionTable, final @GMTransactionTableSortBy int sortBy) {
        if (isViewAttached()) {
            getView().onSearchLoaded(
                    convertToViewModel(getTransactionTable.getCells(), sortBy),
                    (int) getTransactionTable.getTotalCellCount()
            );
        }
    }

    private List<GMStatisticTransactionTableModel> convertToViewModel(List<Cell> datas, final @GMTransactionTableSortBy int sortBy) {
        List<GMStatisticTransactionTableModel> gmStatisticTransactionTableModels =
                new ArrayList<>();
        for (Cell data : datas) {
            GMStatisticTransactionTableModel gmStatisticTransactionTableModel
                    = new GMStatisticTransactionTableModel();
            gmStatisticTransactionTableModel.productName = data.getProductProductName();
            gmStatisticTransactionTableModel.setDeliveredAmount(data.getDeliveredAmt());
            gmStatisticTransactionTableModel.setTransSum(data.getTransSum());
            gmStatisticTransactionTableModel.setOrderSum(data.getOrderSum());
            gmStatisticTransactionTableModel.setProductId(data.getProductProductId());
            gmStatisticTransactionTableModels.add(gmStatisticTransactionTableModel);
        }
        return gmStatisticTransactionTableModels;


    }

    @Override
    public void detachView() {
        super.detachView();
        gmStatGetTransactionTableUseCase.unsubscribe();
    }
}
