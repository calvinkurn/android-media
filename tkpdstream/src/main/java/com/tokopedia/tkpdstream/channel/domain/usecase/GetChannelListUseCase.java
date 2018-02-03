package com.tokopedia.tkpdstream.channel.domain.usecase;

import com.tokopedia.tkpdstream.channel.domain.source.ChannelRepository;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public class GetChannelListUseCase extends UseCase<ChannelListViewModel> {

    ChannelRepository channelRepository;

    @Inject
    public GetChannelListUseCase(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Observable<ChannelListViewModel> createObservable(RequestParams requestParams) {
        return channelRepository.getChannels(requestParams.getParameters());
    }

    public RequestParams createParam() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
