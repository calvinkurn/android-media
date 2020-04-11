package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.List;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void successGetConversation(DetailResChatDomain detailResChatDomain);

        void showProgressBar();

        void dismissProgressBar();

        void dismissChatProgressBar();

        void errorGetConversation(String error);

        void successGetConversationMore(ConversationListDomain conversationListDomain);

        void errorGetConversationMore(String error);

        void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel);

        void errorReplyDiscussion(String error);

        void successAcceptSolution();

        void errorAcceptSolution(String error);

        void successCancelComplaint();

        void errorCancelComplaint(String error);

        void successAskHelp();

        void errorAskHelp(String error);

        void successInputAddress();

        void errorInputAddress(String error);

        void successEditAddress();

        void errorEditAddress(String error);

        void successFinishResolution();

        void errorFinishResolution(String error);

        void initNextStep(NextActionDomain nextActionDomain);

        void initActionButton(ButtonDomain buttonDomain);

        void onRefreshChatAdapter();

        void onAddItemAdapter(List<Visitable> items);

        void onAddItemWithPositionAdapter(int position, List<Visitable> items);
    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView(String resolutionId);
    }
}