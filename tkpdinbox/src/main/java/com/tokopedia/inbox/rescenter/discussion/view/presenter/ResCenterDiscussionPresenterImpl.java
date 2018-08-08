package com.tokopedia.inbox.rescenter.discussion.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GetResCenterDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.LoadMoreDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.GetDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.LoadMoreSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.subscriber.ReplyDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.SendReplyDiscussionParam;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionPresenterImpl implements ResCenterDiscussionPresenter {

    private ResCenterDiscussionView viewListener;
    private GetResCenterDiscussionUseCase getDiscussionUseCase;
    private LoadMoreDiscussionUseCase loadMoreUseCase;
    private SendDiscussionUseCase sendDiscussionUseCase;
    private SendDiscussionV2UseCase sendDiscussionV2UseCase;
    private SendReplyDiscussionParam pass;

    @Inject
    public ResCenterDiscussionPresenterImpl(ResCenterDiscussionView viewListener,
                                            GetResCenterDiscussionUseCase getDiscussionUseCase,
                                            LoadMoreDiscussionUseCase loadMoreDiscussionUseCase,
                                            SendDiscussionUseCase sendDiscussionUseCase,
                                            SendDiscussionV2UseCase sendDiscussionV2UseCase,
                                            SendReplyDiscussionParam sendReplyDiscussionParam) {
        this.viewListener = viewListener;
        this.getDiscussionUseCase = getDiscussionUseCase;
        this.loadMoreUseCase = loadMoreDiscussionUseCase;
        this.sendDiscussionUseCase = sendDiscussionUseCase;
        this.sendDiscussionV2UseCase = sendDiscussionV2UseCase;
        this.pass = sendReplyDiscussionParam;
    }


    @Override
    public void initData() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        getDiscussionUseCase.execute(getDiscussionParams(),
                new GetDiscussionSubscriber(viewListener));

    }

    private RequestParams getDiscussionParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetResCenterDiscussionUseCase.PARAM_RESOLUTION_ID, viewListener.getResolutionID());
        return params;
    }

    @Override
    public void sendReply() {
        viewListener.setViewEnabled(false);
        viewListener.showLoadingProgress();
        if (isValid()) {
            sendDiscussionUseCase.execute(getSendReplyRequestParams(),
                    new ReplyDiscussionSubscriber(viewListener));
        }
    }

    @Override
    public void sendReplySupportVideo() {
        viewListener.setViewEnabled(false);
        viewListener.showLoadingProgress();
        if (isValid()) {
            sendDiscussionV2UseCase.execute(
                    getSendReplyParams(),
                    new ReplyDiscussionSubscriber(viewListener)
            );
        }
    }

    private RequestParams getSendReplyParams() {
        RequestParams params = RequestParams.create();
        params.putString(SendDiscussionV2UseCase.PARAM_RESOLUTION_ID, viewListener.getResolutionID());
        params.putString(SendDiscussionV2UseCase.PARAM_MESSAGE, pass.getMessage());
        params.putInt(SendDiscussionV2UseCase.PARAM_FLAG_RECEIVED, pass.getFlagReceived());
        if (pass.getAttachment() != null && pass.getAttachment().size() > 0) {
            params.putObject(SendDiscussionV2UseCase.PARAM_ATTACHMENT, pass.getAttachment());
        }
        return params;
    }

    private RequestParams getSendReplyRequestParams() {
        RequestParams params = RequestParams.create();
        params.putString(SendDiscussionUseCase.PARAM_MESSAGE, pass.getMessage());
        params.putObject(SendDiscussionUseCase.PARAM_RESOLUTION_ID, pass.getResolutionId());
        params.putInt(SendDiscussionUseCase.PARAM_FLAG_RECEIVED, pass.getFlagReceived());

        if (pass.getAttachment() != null && pass.getAttachment().size() > 0)
            params.putObject(SendDiscussionUseCase.PARAM_ATTACHMENT, pass.getAttachment());
        return params;
    }

    @Override
    public void setDiscussionText(String discussionText) {
        pass.setMessage(discussionText);
    }

    @Override
    public void loadMore() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        loadMoreUseCase.execute(getLoadMoreParam(), new LoadMoreSubscriber(viewListener));
    }

    private RequestParams getLoadMoreParam() {
        RequestParams params = RequestParams.create();
        params.putString(LoadMoreDiscussionUseCase.PARAM_LAST_CONVERSATION_ID, viewListener.getLastConversationId());
        params.putString(LoadMoreDiscussionUseCase.PARAM_RESOLUTION_ID, viewListener.getResolutionID());
        return params;
    }

    @Override
    public void unsubscribeObservable() {
        loadMoreUseCase.unsubscribe();
        getDiscussionUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        sendDiscussionV2UseCase.unsubscribe();
    }

    @Override
    public void setAttachment(ArrayList<AttachmentViewModel> attachmentList) {
        pass.setAttachment(attachmentList);
    }

    @Override
    public void setResolutionId(String resolutionID) {
        pass.setResolutionId(resolutionID);
    }

    @Override
    public void setFlagReceived(boolean flagReceived) {
        pass.setFlagReceived(flagReceived ? 1 : 0);
    }

    private boolean isValid() {
        boolean isValid = true;

        if (pass.getMessage().trim().length() == 0) {
            isValid = false;
            String error = viewListener.getString(R.string.error_field_required);
            viewListener.onErrorSendReply(error);
        }

        return isValid;
    }
}
