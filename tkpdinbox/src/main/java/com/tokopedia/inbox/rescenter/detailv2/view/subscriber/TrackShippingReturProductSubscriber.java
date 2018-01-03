package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milhamj on 24/11/17.
 */

public class TrackShippingReturProductSubscriber extends rx.Subscriber<TrackingAwbReturProduct>{

    private final TrackShippingFragmentListener.View view;

    public TrackShippingReturProductSubscriber(TrackShippingFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement element = e.getStackTrace()[i];
            Logger.dump(this.getClass().getSimpleName(), element.toString());
        }
        if (e instanceof IOException) {
            view.onTrackingTimeOut();
        } else {
            view.onTrackingFailed();
        }
    }

    @Override
    public void onNext(TrackingAwbReturProduct object) {
        if (object.isSuccess()) {
            view.onTrackingSuccess(mappingViewModel(object));
        } else {
            view.onTrackingError(object.getMessageError());
        }
    }

    private TrackingDialogViewModel mappingViewModel(TrackingAwbReturProduct domainData) {
        TrackingDialogViewModel model = new TrackingDialogViewModel();
        if (domainData != null && domainData.isSuccess()) {
            model.setSuccess(true);
            model.setDelivered(domainData.isDelivered());
            model.setReceiverName(domainData.getReceiverName());
            model.setShippingRefNum(domainData.getShippingRefNum());
            model.setTrackHistory(
                    domainData.getTrackingHistory() != null ?
                            mappingTrackHistory(domainData.getTrackingHistory()) : null
            );
        } else {
            model.setSuccess(false);
            model.setMessageError(domainData != null ? domainData.getMessageError() : null);
        }
        return model;
    }

    private List<TrackingHistoryDialogViewModel> mappingTrackHistory(List<TrackingAwbReturProductHistory> domainModels) {
        List<TrackingHistoryDialogViewModel> viewModels = new ArrayList<>();
        for (TrackingAwbReturProductHistory items : domainModels) {
            TrackingHistoryDialogViewModel model = new TrackingHistoryDialogViewModel();
            model.setCity(items.getCity());
            model.setDate(items.getDate());
            model.setStatus(items.getStatus());
            viewModels.add(model);
        }
        return viewModels;
    }
}
