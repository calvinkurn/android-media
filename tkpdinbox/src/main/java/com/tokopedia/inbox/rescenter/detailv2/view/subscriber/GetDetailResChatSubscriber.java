package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/10/17.
 */

public class GetDetailResChatSubscriber extends Subscriber<DetailResChatDomain> {

    public static final String CREATE = "create";
    public static final String EDIT_SOLUTION = "edit_solution";
    public static final String REPLY_CONVERSATION = "reply_conversation";
    public static final String ACCEPT_ADMIN_SOLUTION = "accept_admin_resolution";
    public static final String ACTION_EARLY = "action_early";
    public static final String ACTION_FINAL = "action_final";
    public static final String APPEAL_RESOLUTION = "appeal_resolution";
    public static final String CANCEL = "cancel";
    public static final String EDIT_ADDRESS = "edit_address";
    public static final String EDIT_RESI = "edit_resi";
    public static final String FINISH = "finish";
    public static final String HANDLE_RESOLUTION = "handle_resolution";
    public static final String INPUT_ADDRESS = "input_address";
    public static final String INPUT_RESI = "input_resi";
    public static final String REJECT_ADMIN_RESOLUTION = "reject_admin_resolution";
    public static final String REPORT_RESOLUTION = "report_resolution";
    public static final String ENTER_RETUR_SESSION_RESOLUTION = "enter_retur_session_resolution";
    public static final String ACTION_RESET = "action_reset";

    private final DetailResChatFragmentListener.View mainView;
    private boolean isFirstInit;

    public GetDetailResChatSubscriber(DetailResChatFragmentListener.View mainView, boolean isFirstInit) {
        this.mainView = mainView;
        this.isFirstInit = isFirstInit;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissProgressBar();
        e.printStackTrace();
        mainView.errorGetConversation(ErrorHandler.getErrorMessage(e), isFirstInit);
    }

    @Override
    public void onNext(DetailResChatDomain detailResChatDomain) {
        mainView.dismissProgressBar();
        mainView.successGetConversation(detailResChatDomain, isFirstInit);
        initAllData(detailResChatDomain);
    }

    private void initAllData(DetailResChatDomain detailResChatDomain) {
        mainView.initNextStep(detailResChatDomain.getNextAction());
        mainView.initActionButton(detailResChatDomain.getButton());
        initChatData(detailResChatDomain, isFirstInit);
    }


    private void initChatData(DetailResChatDomain detailResChatDomain, boolean isFirstInit) {
        int lastAction = 0;
        List<Visitable> items = new ArrayList<>();
        for (ConversationDomain conversationDomain : detailResChatDomain.getConversationList().getConversationDomains()) {
            String actionType = conversationDomain.getAction().getType();
            if (actionType.equals(CREATE)) {
                items.add(new ChatCreateLeftViewModel(
                        detailResChatDomain.getShop(),
                        detailResChatDomain.getLast(),
                        conversationDomain));
            } else if (actionType.equals(REPLY_CONVERSATION)) {
                boolean isShowTitle;
                if (lastAction == conversationDomain.getAction().getBy()) {
                    isShowTitle = false;
                } else {
                    lastAction = conversationDomain.getAction().getBy();
                    isShowTitle = true;
                }
                if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                    items.add(new ChatRightViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain));
                } else {
                    items.add(new ChatLeftViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain,
                            isShowTitle));
                }
            } else if (actionType.equals(EDIT_SOLUTION)) {
                boolean isShowTitle;
                if (lastAction == conversationDomain.getAction().getBy()) {
                    isShowTitle = false;
                } else {
                    lastAction = conversationDomain.getAction().getBy();
                    isShowTitle = true;
                }
                if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                    items.add(new ChatSystemRightViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain));
                } else {
                    items.add(new ChatSystemLeftViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain,
                            isShowTitle));
                }
            } else if (actionType.equals(REPORT_RESOLUTION)
                    || actionType.equals(ENTER_RETUR_SESSION_RESOLUTION)
                    || actionType.equals(FINISH)
                    || actionType.equals(REJECT_ADMIN_RESOLUTION)
                    || actionType.equals(CANCEL)
                    || actionType.equals(ACCEPT_ADMIN_SOLUTION)) {
                items.add(new ChatCommonLeftViewModel(conversationDomain, actionType));
            } else {
                if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                    items.add(new ChatNotSupportedRightViewModel(
                            conversationDomain));
                } else {
                    items.add(new ChatNotSupportedLeftViewModel(
                            conversationDomain));
                }
            }
        }
        mainView.onAddItemAdapter(items);
        mainView.onRefreshChatAdapter(isFirstInit);
    }
}
