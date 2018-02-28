package com.tokopedia.tkpdstream.channel.view.subscriber;

import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import rx.Subscriber;

/**
 * @author by nisie on 2/28/18.
 */

public class RefreshChannelSubscriber extends Subscriber<ChannelListViewModel> {
    private final ChannelContract.View view;

    public RefreshChannelSubscriber(ChannelContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorRefreshChannel(GroupChatErrorHandler.getErrorMessage(view.getContext(), e,
                true));
    }

    @Override
    public void onNext(ChannelListViewModel channelListViewModel) {
        view.onSuccessRefreshChannel(channelListViewModel);
    }
}
