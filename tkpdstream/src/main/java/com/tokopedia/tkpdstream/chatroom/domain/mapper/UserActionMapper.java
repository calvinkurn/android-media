package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.sendbird.android.User;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class UserActionMapper {

    public UserActionViewModel mapUserEnter(User user) {
        return new UserActionViewModel(user.getUserId(),
                user.getFriendName(), user.getProfileUrl(), UserActionViewModel.ACTION_ENTER);
    }


}
