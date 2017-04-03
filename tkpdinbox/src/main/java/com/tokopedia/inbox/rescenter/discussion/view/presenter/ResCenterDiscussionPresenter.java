package com.tokopedia.inbox.rescenter.discussion.view.presenter;

import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/29/17.
 */

public interface ResCenterDiscussionPresenter {
    void initData();

    void sendReply();

    void setDiscussionText(String discussionText);

    void loadMore();

    void unsubscribeObservable();

    void setAttachment(ArrayList<AttachmentViewModel> attachmentList);
}
