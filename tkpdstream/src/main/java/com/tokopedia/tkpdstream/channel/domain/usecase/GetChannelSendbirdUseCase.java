package com.tokopedia.tkpdstream.channel.domain.usecase;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

/**
 * @author by nisie on 2/6/18.
 */

public class GetChannelSendbirdUseCase {

    private interface SendbirdChannelListener {
        void onGetChannel(OpenChannel openChannel);

        void onErrorGetChannel(SendBirdException e);

        void onErrorEnterChannel(SendBirdException e);
    }

    public GetChannelSendbirdUseCase() {
    }


    public void execute(String channelUrl,
                        final GetChannelSendbirdUseCase.SendbirdChannelListener listener) {
        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    listener.onErrorGetChannel(e);
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            listener.onErrorEnterChannel(e);
                        }
                    }
                });
            }
        });
    }
}