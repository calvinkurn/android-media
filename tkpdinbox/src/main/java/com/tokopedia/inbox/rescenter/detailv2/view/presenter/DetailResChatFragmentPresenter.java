package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.ReplyDiscussionSubscriber;

import javax.inject.Inject;

/**
 * Created by yoasfs on 09/10/17.
 */

public class DetailResChatFragmentPresenter
        extends BaseDaggerPresenter<DetailResChatFragmentListener.View>
        implements DetailResChatFragmentListener.Presenter {

    public static final int PARAM_MIN_REPLY_CHAR_COUNT = 7;
    public static final int PARAM_MAX_REPLY_CHAR_COUNT = 5000;

    DetailResChatFragmentListener.View mainView;

    GetResChatUseCase getResChatUseCase;
    SendDiscussionUseCase sendDiscussionUseCase;
    SendDiscussionV2UseCase sendDiscussionV2UseCase;

    String resolutionId;

    @Inject
    public DetailResChatFragmentPresenter(GetResChatUseCase getResChatUseCase,
                                          SendDiscussionUseCase sendDiscussionUseCase,
                                          SendDiscussionV2UseCase sendDiscussionV2UseCase) {
        this.getResChatUseCase = getResChatUseCase;
        this.sendDiscussionUseCase = sendDiscussionUseCase;
        this.sendDiscussionV2UseCase = sendDiscussionV2UseCase;
    }

    @Override
    public void attachView(DetailResChatFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }


    @Override
    public void detachView() {
        getResChatUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        sendDiscussionV2UseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        this.resolutionId = resolutionId;
        loadConversation(resolutionId);
    }

    public void loadConversation(String resolutionId) {
        mainView.showProgressBar();
        getResChatUseCase.execute(
                getResChatUseCase.getResChatUseCaseParam(String.valueOf(resolutionId)),
                new GetDetailResChatSubscriber(mainView));
    }

    @Override
    public void sendIconPressed(String message) {
        if (message.length() >= PARAM_MIN_REPLY_CHAR_COUNT && message.length() <= PARAM_MAX_REPLY_CHAR_COUNT) {
            sendMessageWithoutAttachment(message);
        } else {
            mainView.errorInputMessage("Minimal 7 karakter dan maksimal 5000 karakter");
        }
    }

    private void sendMessageWithoutAttachment(String message) {
        sendDiscussionV2UseCase.execute(
                SendDiscussionV2UseCase
                        .getSendReplyParamsWithoutAttachment(resolutionId, message),
                new ReplyDiscussionSubscriber(mainView));
    }
}
