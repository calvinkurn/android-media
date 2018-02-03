package com.tokopedia.tkpdstream.channel.view.subscriber;

import com.tokopedia.abstraction.common.utils.ErrorHandler;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 2/3/18.
 */

public class GetChannelFirstTimeSubscriber extends Subscriber<ChannelListViewModel> {
    private final ChannelContract.View view;

    public GetChannelFirstTimeSubscriber(ChannelContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onFailedGetChannelFirstTime(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(ChannelListViewModel channelListViewModel) {
        view.onSuccessGetChannelFirstTime(channelListViewModel);
    }
}
