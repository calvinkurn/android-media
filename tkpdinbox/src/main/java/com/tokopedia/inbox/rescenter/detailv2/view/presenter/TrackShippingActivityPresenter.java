package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detail.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.NextActionFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.TrackShippingFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.NextActionActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

/**
 * Created by milhamj on 24/11/17.
 */

public class TrackShippingActivityPresenter implements TrackShippingActivityListener.Presenter {

    private TrackShippingActivityListener.View mainView;
    private String shipmentId;
    private String shippingRef;

    public TrackShippingActivityPresenter(TrackShippingActivityListener.View mainView,
                                          String shipmentId,
                                          String shippingRef) {
        this.mainView = mainView;
        this.shipmentId = shipmentId;
        this.shippingRef = shippingRef;
    }

    @Override
    public void attachView(TrackShippingActivityListener.View view) {
        this.mainView = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment() {
        mainView.inflateFragment(TrackShippingFragment.newInstance(shipmentId, shippingRef),
                TrackShippingFragment.class.getSimpleName());
    }
}
