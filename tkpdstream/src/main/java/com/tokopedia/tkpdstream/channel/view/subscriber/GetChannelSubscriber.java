package com.tokopedia.tkpdstream.channel.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;

import rx.Subscriber;

/**
 * Created by StevenFredian on 13/02/18.
 */

public class GetChannelSubscriber extends Subscriber<ChannelListViewModel> {

    private final ChannelContract.View view;

    public GetChannelSubscriber(ChannelContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onFailedGetChannel(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(ChannelListViewModel channelListViewModel) {
        view.onSuccessGetChannel(channelListViewModel);
    }
}
