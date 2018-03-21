package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;

/**
 * @author by nisie on 3/21/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

        void onErrorGetChannelInfo(String errorMessage);

        void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel);
    }

    interface Presenter extends CustomerPresenter<GroupChatContract.View> {

        void getChannelInfo(String channelUuid);

        void logoutChannel(OpenChannel mChannel);

        void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                          LoginGroupChatUseCase.LoginGroupChatListener
                                  loginGroupChatListener);
    }
}
