package com.tokopedia.inbox.rescenter.discussion.view.listener;

import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.List;

/**
 * Created by nisie on 3/29/17.
 */

public interface ResCenterDiscussionView {
    void onSuccessGetDiscussion(List<DiscussionItemViewModel> discussionData,
                                boolean canLoadMore);

    void onSuccessSendDiscussion();

    String getResolutionID();

    void onErrorSendReply(String errorMessage);

    void finishLoading();

    void showLoading();

    void setViewEnabled(boolean isEnabled);

    void onErrorGetDiscussion(String errorMessage);

    String getString(int resId);

    void onSuccessLoadMore(List<DiscussionItemViewModel> discussionItemViewModels, boolean canLoadMore);

    String getLastConversationId();
}
