package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.TrackShippingReturProductSubscriber;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;

import javax.inject.Inject;

/**
 * Created by milhamj on 24/11/17.
 */

public class TrackShippingFragmentPresenter
        extends BaseDaggerPresenter<TrackShippingFragmentListener.View>
        implements TrackShippingFragmentListener.Presenter {
    TrackShippingFragmentListener.View mainView;

    private final TrackAwbReturProductUseCase trackAwbReturProductUseCase;

    @Inject
    public TrackShippingFragmentPresenter(TrackAwbReturProductUseCase trackAwbReturProductUseCase) {
        this.trackAwbReturProductUseCase = trackAwbReturProductUseCase;
    }

    @Override
    public void attachView(TrackShippingFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        trackAwbReturProductUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void initPresenter(String shipmentId, String shippingRef) {
        doTrackShipping(shipmentId, shippingRef);
    }

    @Override
    public void doTrackShipping(String shipmentId, String shippingRef) {
        mainView.showLoading();
        trackAwbReturProductUseCase.execute(getTrackingAwbProductParam(shipmentId, shippingRef),
                new TrackShippingReturProductSubscriber(mainView));
    }

    private RequestParams getTrackingAwbProductParam(String shipmentID, String shipmentRef) {
        RequestParams params = RequestParams.create();
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPMENT_ID, shipmentID);
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPPING_REFENCE, shipmentRef);
        return params;
    }
}
