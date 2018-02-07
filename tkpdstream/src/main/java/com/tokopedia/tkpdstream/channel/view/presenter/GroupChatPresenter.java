package com.tokopedia.tkpdstream.channel.view.presenter;

import android.util.Log;

import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpdstream.channel.domain.ConnectionManager;
import com.tokopedia.tkpdstream.channel.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.channel.view.listener.GroupChatContract;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;

    @Inject
    public GroupChatPresenter(
                              GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase) {
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;

    }

    @Override
    public void initMessageFirstTime() {
        ConnectionManager.addConnectionManagementHandler("NISNIS1", ConnectionManager
                .CONNECTION_HANDLER_ID, new ConnectionManager.ConnectionManagementHandler() {
            @Override
            public void onConnected(boolean reconnect) {
                if (reconnect) {
                    getMessages();
                } else {
                    getMessagesFirstTime();
                }
            }
        });
    }

    private void getMessagesFirstTime() {
        getGroupChatMessagesFirstTimeUseCase.execute("LALALA",
                new GetGroupChatMessagesFirstTimeUseCase.SendbirdChannelListener() {
                    @Override
                    public void onGetMessages() {
                        Log.d("NISNIS", "onGetMessages");
                    }

                    @Override
                    public void onErrorGetMessagesFirstTime(SendBirdException e) {
                        Log.d("NISNIS", "onErrorGetMessagesFirstTime " + e.getLocalizedMessage());
                    }
                });
    }

    private void getMessages() {

    }
}
