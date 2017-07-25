package com.tokopedia.ride.ontrip.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by alvarisi on 6/14/17.
 */

public interface SendCancelReasonContract {
    interface View extends CustomerView{

        void showLoading();

        RequestParams getReasonsParam();

        void hideLoading();

        void renderReasons(List<String> reasons);

        void hideMainLayout();

        void showMainLayout();

        RequestParams getCancelParams();

        void showErrorCancelRequest();

        void onSuccessCancelRequest();

        String getSelectedReason();

        void showReasonEmptyError();

        void showErrorGetReasons();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void actionGetReasons();

        void submitReasons();
    }
}
