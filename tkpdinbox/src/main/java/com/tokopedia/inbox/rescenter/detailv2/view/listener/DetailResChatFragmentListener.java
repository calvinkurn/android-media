package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void populateView(DetailResChatDomain detailResChatDomain);

        void successGetConversation(DetailResChatDomain detailResChatDomain);

        void showProgressBar();

        void dismissProgressBar();

        void errorGetConversation(String error);

        void errorInputMessage(String error);

        void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel);

        void errorReplyDiscussion(String error);
    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView(String resolutionId);

        void sendIconPressed(String message);
    }
}