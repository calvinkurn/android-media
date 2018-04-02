package com.tokopedia.tkpdstream.channel.view.subscriber;

import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import rx.Subscriber;

/**
 * @author by StevenFredian on 13/02/18.
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
        view.hideLoading();
        view.onFailedGetChannel(GroupChatErrorHandler.getErrorMessage(view.getContext(), e, true));
    }

    @Override
    public void onNext(ChannelListViewModel channelListViewModel) {
        view.hideLoading();
        view.onSuccessGetChannel(channelListViewModel);
    }
}
