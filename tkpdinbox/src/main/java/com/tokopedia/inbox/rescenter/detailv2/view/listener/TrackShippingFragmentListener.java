package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;

/**
 * Created by milhamj on 24/11/17.
 */

public interface TrackShippingFragmentListener {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onTrackingTimeOut();

        void onTrackingSuccess(TrackingDialogViewModel trackingDialogViewModel);

        void onTrackingError(String messageError);

        void onTrackingFailed();
    }

    interface Presenter extends CustomerPresenter<TrackShippingFragmentListener.View> {
        void initPresenter(String shipmentId, String shippingRef);

        void doTrackShipping(String shipmentId, String shippingRef);
    }
}
