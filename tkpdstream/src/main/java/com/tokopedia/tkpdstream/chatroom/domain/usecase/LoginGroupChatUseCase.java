package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;

import javax.inject.Inject;

/**
 * @author by nisie on 2/14/18.
 */

public class LoginGroupChatUseCase {

    public interface LoginGroupChatListener {
        void onSuccessEnterChannel(OpenChannel openChannel);

        void onErrorEnterChannel(SendBirdException e);
    }

    @Inject
    public LoginGroupChatUseCase() {
    }

    public void execute(String channelUrl,
                        final LoginGroupChatListener listener) {
        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    listener.onErrorEnterChannel(e);
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {

                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            listener.onErrorEnterChannel(e);
                        }

                        listener.onSuccessEnterChannel(openChannel);

                    }
                });
            }
        });
    }
}
