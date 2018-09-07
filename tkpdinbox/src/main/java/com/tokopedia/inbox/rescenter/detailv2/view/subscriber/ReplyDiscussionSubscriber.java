package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import rx.Subscriber;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionSubscriber extends Subscriber<DiscussionItemViewModel> {

    private DetailResChatFragmentListener.View mainView;

    public ReplyDiscussionSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.errorReplyDiscussion(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DiscussionItemViewModel discussionItemViewModel) {
        mainView.successReplyDiscussion(discussionItemViewModel);
    }
}
