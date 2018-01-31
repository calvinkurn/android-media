package com.tokopedia.tokocash.historytokocash.presentation.contract;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tokocash.historytokocash.presentation.model.HeaderHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface TokoCashHistoryContract {

    interface View extends CustomerView {

        void hideLoading();

        void setHasNextPage(boolean hasNextPage);

        void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad);

        void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList);

        void renderErrorMessage(String message);

        void renderEmptyPage(String message);

        void renderWaitingTransaction(TokoCashHistoryData tokoCashHistoryData);

        void hideWaitingTransaction();

        RequestParams getHistoryTokoCashParam(boolean isWaitingTransaction, int page);
    }

    interface Presenter extends CustomerPresenter<View>{

        void getWaitingTransaction();

        void getInitHistoryTokoCash();

        void getHistoryLoadMore();

        void onDestroyPresenter();
    }
}
