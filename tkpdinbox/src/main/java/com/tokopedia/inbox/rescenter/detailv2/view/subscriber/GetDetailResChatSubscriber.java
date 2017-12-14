package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionEarlyLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionFinalLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionResetLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.CustomerDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ShopDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/10/17.
 */

public class GetDetailResChatSubscriber extends Subscriber<DetailResChatDomain> {

    public static final String TAG = GetDetailResChatSubscriber.class.getSimpleName();
    public static final String CREATE = "create";
    public static final String RECOMPLAIN = "recomplain";
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

    public GetDetailResChatSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    public static List<Visitable> initChatData(ConversationListDomain conversationListDomain,
                                               ShopDomain shopDomain,
                                               CustomerDomain customerDomain,
                                               LastDomain lastDomain,
                                               int actionBy) {
        int lastAction = 0; //only used for reply conversation, so need to be reset when hit another type
        List<Visitable> items = new ArrayList<>();
        for (ConversationDomain conversationDomain : conversationListDomain.getConversationDomains()) {
            String actionType = conversationDomain.getAction().getType();
            if (actionType.equals(CREATE) || actionType.equals(RECOMPLAIN)) {
                lastAction = 0;
                items.add(new ChatCreateLeftViewModel(
                        shopDomain,
                        lastDomain,
                        conversationDomain,
                        actionType));
            } else if (actionType.equals(REPLY_CONVERSATION)) {
                if (actionBy == conversationDomain.getAction().getBy()) {
                    items.add(new ChatRightViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain));
                } else {
                    items.add(new ChatLeftViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain,
                            lastAction != conversationDomain.getAction().getBy()));
                }
                lastAction = conversationDomain.getAction().getBy();
            } else if (actionType.equals(EDIT_SOLUTION) || actionType.equals(APPEAL_RESOLUTION)) {
                lastAction = 0;
                if (actionBy == conversationDomain.getAction().getBy()) {
                    items.add(new ChatSystemRightViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain,
                            actionType));
                } else {
                    items.add(new ChatSystemLeftViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain,
                            actionType));
                }
            } else if (actionType.equals(ACTION_RESET)) {
                items.add(new ChatActionResetLeftViewModel(
                        shopDomain,
                        customerDomain,
                        conversationDomain));
            } else if (actionType.equals(INPUT_ADDRESS) || actionType.equals(EDIT_ADDRESS)) {
                lastAction = 0;
                if (actionBy == conversationDomain.getAction().getBy()) {
                    items.add(new ChatInputAddressRightViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain,
                            MainApplication.getAppContext().getResources().getString(R.string.string_address_format)));
                } else {
                    items.add(new ChatInputAddressLeftViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain,
                            MainApplication.getAppContext().getResources().getString(R.string.string_address_format)));
                }
            } else if (actionType.equals(INPUT_RESI) || actionType.equals(EDIT_RESI)) {
                lastAction = 0;
                if (actionBy == conversationDomain.getAction().getBy()) {
                    items.add(new ChatAwbRightViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain));
                } else {
                    items.add(new ChatAwbLeftViewModel(
                            shopDomain,
                            customerDomain,
                            conversationDomain));
                }
            } else if (actionType.equals(ACTION_FINAL)) {
                lastAction = 0;
                items.add(new ChatActionFinalLeftViewModel(conversationDomain, actionType));
            } else if (actionType.equals(ACTION_EARLY)) {
                lastAction = 0;
                items.add(new ChatActionEarlyLeftViewModel(conversationDomain, actionType));
            } else if (actionType.equals(REPORT_RESOLUTION)
                    || actionType.equals(ENTER_RETUR_SESSION_RESOLUTION)
                    || actionType.equals(FINISH)
                    || actionType.equals(REJECT_ADMIN_RESOLUTION)
                    || actionType.equals(CANCEL)
                    || actionType.equals(ACCEPT_ADMIN_SOLUTION)) {
                lastAction = 0;
                items.add(new ChatCommonLeftViewModel(conversationDomain, actionType));
            } else {
                Log.e(TAG, "initChatData: not supported type " + actionType , null );
            }
        }
        return items;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissProgressBar();
        e.printStackTrace();
        mainView.errorGetConversation(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DetailResChatDomain detailResChatDomain) {
        mainView.dismissProgressBar();
        mainView.successGetConversation(detailResChatDomain);
        initAllData(detailResChatDomain);
    }

    private void initAllData(DetailResChatDomain detailResChatDomain) {
        mainView.initNextStep(detailResChatDomain.getNextAction());
        mainView.initActionButton(detailResChatDomain.getButton());
        mainView.onAddItemAdapter(initChatData(
                detailResChatDomain.getConversationList(),
                detailResChatDomain.getShop(),
                detailResChatDomain.getCustomer(),
                detailResChatDomain.getLast(),
                detailResChatDomain.getActionBy()));
        mainView.onRefreshChatAdapter();
    }
}
