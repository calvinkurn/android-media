package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import java.util.List;

import rx.Subscriber;

/**
 * Created by milhamj on 22/11/17.
 */

public class GetDetailResChatMoreSubscriber extends Subscriber<ConversationListDomain> {

    private final static int TOP_POSITION = 0;
    private final DetailResChatFragmentListener.View mainView;
    private final DetailResChatDomain detailResChatDomain;

    public GetDetailResChatMoreSubscriber(DetailResChatFragmentListener.View mainView,
                                          DetailResChatDomain detailResChatDomain) {
        this.mainView = mainView;
        this.detailResChatDomain = detailResChatDomain;
    }

    @Override
    public void onNext(ConversationListDomain conversationListDomain) {
        mainView.dismissChatProgressBar();
        mainView.successGetConversationMore(conversationListDomain);
        List<Visitable> visitableList = GetDetailResChatSubscriber.initChatData(conversationListDomain,
                detailResChatDomain.getShop(),
                detailResChatDomain.getCustomer(),
                detailResChatDomain.getLast(),
                detailResChatDomain.getActionBy());
        mainView.onAddItemWithPositionAdapter(TOP_POSITION, visitableList);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissChatProgressBar();
        e.printStackTrace();
        mainView.errorGetConversationMore(ErrorHandler.getErrorMessage(e));
    }
}
