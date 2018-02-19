package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;

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

    public void execute(final String channelUrl,
                        String userId,
                        final String userName, final String userAvatar, final LoginGroupChatListener listener) {

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    listener.onErrorEnterChannel(e);
                    return;
                }

                SendBird.updateCurrentUserInfo(userName, userAvatar, new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            listener.onErrorEnterChannel(e);
                            return;
                        }

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
                });
            }
        });

    }
}
