package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.sendbird.android.User;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 2/22/18.
 */

public class UserActionMapper {

    @Inject
    public UserActionMapper() {
    }

    public UserActionViewModel mapUserEnter(User user) {
        return new UserActionViewModel(user.getUserId(),
                user.getNickname(), user.getProfileUrl(), UserActionViewModel.ACTION_ENTER);
    }


    public UserActionViewModel mapUserExit(User user) {
        return new UserActionViewModel(user.getUserId(),
                user.getNickname(), user.getProfileUrl(), UserActionViewModel.ACTION_EXIT);
    }
}
