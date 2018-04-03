package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;

import javax.inject.Inject;

/**
 * @author by nisie on 2/15/18.
 */

public class LogoutGroupChatUseCase {

    @Inject
    public LogoutGroupChatUseCase() {
    }

    public void execute(OpenChannel mChannel) {
        if (mChannel != null) {
            mChannel.exit(new OpenChannel.OpenChannelExitHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                }
            });
        }
    }
}
