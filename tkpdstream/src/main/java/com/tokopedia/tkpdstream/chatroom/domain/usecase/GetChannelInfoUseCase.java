package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.tokopedia.tkpdstream.chatroom.domain.source.ChannelInfoSource;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/22/18.
 */

public class GetChannelInfoUseCase extends UseCase<ChannelInfoViewModel> {


    public static final String PARAM_CHANNEL_UUID = "channel_uuid";

    private ChannelInfoSource channelInfoSource;

    @Inject
    public GetChannelInfoUseCase(ChannelInfoSource channelInfoSource) {
        this.channelInfoSource = channelInfoSource;
    }

    @Override
    public Observable<ChannelInfoViewModel> createObservable(RequestParams requestParams) {
        return channelInfoSource.getChannelInfo(requestParams.getString(
                GetChannelInfoUseCase.PARAM_CHANNEL_UUID, ""));
    }

    public static RequestParams createParams(String channelUuid) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_CHANNEL_UUID, channelUuid);
        return params;
    }
}
