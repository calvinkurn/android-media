package com.tokopedia.tkpdstream.channel.view.subscriber;

import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

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
        view.dismissLoadingFull();
        view.onFailedGetChannelFirstTime(GroupChatErrorHandler.getErrorMessage(view.getContext(),
                e, false));
    }

    @Override
    public void onNext(ChannelListViewModel channelListViewModel) {
        view.dismissLoadingFull();
        view.onSuccessGetChannelFirstTime(channelListViewModel);
    }
}
