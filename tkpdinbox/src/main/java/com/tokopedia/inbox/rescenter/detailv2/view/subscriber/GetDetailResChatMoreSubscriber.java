package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by milhamj on 22/11/17.
 */

public class GetDetailResChatMoreSubscriber extends Subscriber<ConversationListDomain> {

    private final DetailResChatFragmentListener.View mainView;

    public GetDetailResChatMoreSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onNext(ConversationListDomain conversationListDomain) {
        mainView.dismissProgressBar();
        Log.d("MyTag", "detailResChatDomain " + conversationListDomain);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissProgressBar();
        e.printStackTrace();
        mainView.errorGetConversationMore(ErrorHandler.getErrorMessage(e));
        Log.d("MyTag", "error " + e.getLocalizedMessage());
    }
}
