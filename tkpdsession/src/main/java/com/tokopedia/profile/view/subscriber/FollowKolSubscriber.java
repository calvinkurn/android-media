package com.tokopedia.profile.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;

import rx.Subscriber;

/**
 * @author by milhamj on 01/03/18.
 */

public class FollowKolSubscriber extends Subscriber<Boolean> {

    private final TopProfileActivityListener.View view;

    public FollowKolSubscriber(TopProfileActivityListener.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.hideLoading();
        view.onErrorFollowKol(
                ErrorHandler.getErrorMessage(view.getContext(), throwable)
        );
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.hideLoading();
        if (isSuccess) {
            view.onSuccessFollowKol();
        } else {
            view.onErrorFollowKol(null);
        }
    }
}
