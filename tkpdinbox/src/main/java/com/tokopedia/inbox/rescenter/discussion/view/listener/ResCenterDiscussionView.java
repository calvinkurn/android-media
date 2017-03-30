package com.tokopedia.inbox.rescenter.discussion.view.listener;

import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionData;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.ResCenterDiscussionItemViewModel;

import java.util.List;

/**
 * Created by nisie on 3/29/17.
 */

public interface ResCenterDiscussionView {
    void onSuccessGetDiscussion(List<ResCenterDiscussionItemViewModel> discussionData);

    void onSuccessSendDiscussion();

    String getResolutionID();

    void onErrorSendReply(String errorMessage);

    void finishLoading();

    void showLoading();

    void setViewEnabled(boolean isEnabled);

    void onErrorGetDiscussion(String errorMessage);

    String getString(int resId);
}
