package com.tokopedia.tkpdstream.channel.domain.usecase;

import com.tokopedia.tkpdstream.channel.domain.source.ChannelSource;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public class GetChannelListUseCase extends UseCase<ChannelListViewModel> {

    private ChannelSource channelSource;

    @Inject
    public GetChannelListUseCase(ChannelSource channelSource) {
        this.channelSource = channelSource;
    }

    @Override
    public Observable<ChannelListViewModel> createObservable(RequestParams requestParams) {
        return channelSource.getChannels(requestParams.getParameters());
    }

    public RequestParams createParamFirstTime() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

    public RequestParams createParam() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
