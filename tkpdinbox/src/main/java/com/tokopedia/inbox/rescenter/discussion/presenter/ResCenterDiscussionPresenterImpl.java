package com.tokopedia.inbox.rescenter.discussion.presenter;

import com.tokopedia.inbox.rescenter.discussion.listener.ResCenterDiscussionView;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionPresenterImpl implements ResCenterDiscussionPresenter {

    private final ResCenterDiscussionView viewListener;

    public ResCenterDiscussionPresenterImpl(ResCenterDiscussionView viewListener) {
        this.viewListener = viewListener;
    }


    @Override
    public void initData() {
        viewListener.onSuccessGetDiscussion();
    }

    @Override
    public void sendDiscussion() {
        if(isValid()){
            viewListener.onSuccessSendDiscussion();
        }
    }

    @Override
    public void setDiscussionText(String discussionText) {

    }

    private boolean isValid() {
        boolean isValid = true;

        return isValid;
    }
}
