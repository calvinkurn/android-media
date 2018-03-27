package com.tokopedia.tkpdstream.chatroom.view.presenter;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 3/21/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetChannelInfoUseCase getChannelInfoUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetChannelInfoUseCase getChannelInfoUseCase,
                              LogoutGroupChatUseCase logoutGroupChatUseCase,
                              ChannelHandlerUseCase channelHandlerUseCase) {
        this.getChannelInfoUseCase = getChannelInfoUseCase;
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.logoutGroupChatUseCase = logoutGroupChatUseCase;
        this.channelHandlerUseCase = channelHandlerUseCase;
    }

    @Override
    public void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                             LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener, String sendBirdToken) {
        loginGroupChatUseCase.execute(getView().getContext(), channelUrl, userId, userName,
                userAvatar, loginGroupChatListener, sendBirdToken);
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {
        logoutGroupChatUseCase.execute(mChannel);
    }

    @Override
    public void getChannelInfo(String channelUuid) {
        getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(channelUuid),
                new Subscriber<ChannelInfoViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorGetChannelInfo(GroupChatErrorHandler.getErrorMessage(
                                    getView().getContext(), e, false
                            ));
                        }
                    }

                    @Override
                    public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                        if (getView() != null) {
                            getView().onSuccessGetChannelInfo(channelInfoViewModel);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        getChannelInfoUseCase.unsubscribe();
    }

    public void setHandler(String channelUrl, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, listener);
    }
}
