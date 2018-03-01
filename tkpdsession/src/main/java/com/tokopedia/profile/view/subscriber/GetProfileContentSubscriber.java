package com.tokopedia.profile.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.profile.view.listener.ProfileContentListener;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by nakama on 28/02/18.
 */

public class GetProfileContentSubscriber extends Subscriber<TopProfileViewModel>{

    private final ProfileContentListener.View view;

    public GetProfileContentSubscriber(ProfileContentListener.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.hideLoading();
        view.onErrorGetProfileData(
                ErrorHandler.getErrorMessage(view.getContext(), throwable)
        );
    }

    @Override
    public void onNext(TopProfileViewModel topProfileViewModel) {
        view.hideLoading();
        view.onSuccessGetProfileData(topProfileViewModel);
    }
}
