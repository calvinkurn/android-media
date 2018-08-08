package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/22/17.
 */

public class ResolutionActionSubscriber extends Subscriber<ResolutionActionDomainData> {

    private final DetailResCenterFragmentView fragmentView;
    private String action;

    public ResolutionActionSubscriber(DetailResCenterFragmentView fragmentView, String action) {
        this.fragmentView = fragmentView;
        this.action = action;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            fragmentView.doOnActionTimeOut();
        } else {
            fragmentView.doOnActionError();
        }
    }

    @Override
    public void onNext(ResolutionActionDomainData data) {
        if (data.isSuccess()) {
            fragmentView.doOnActionSuccess(action);
        } else {
            fragmentView.doOnActionError(data.getMessageError());
        }
    }
}
