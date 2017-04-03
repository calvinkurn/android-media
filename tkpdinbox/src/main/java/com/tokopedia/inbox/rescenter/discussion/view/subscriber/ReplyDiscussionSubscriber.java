package com.tokopedia.inbox.rescenter.discussion.view.subscriber;

import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;

import rx.Subscriber;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionSubscriber extends Subscriber<ActionDiscussionModel> {

    public ReplyDiscussionSubscriber(ResCenterDiscussionView viewListener) {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ActionDiscussionModel actionDiscussionModel) {

    }
}
