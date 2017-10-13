package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/10/17.
 */

public class GetDetailResChatSubscriber extends Subscriber<DetailResChatDomain> {
    private final DetailResChatFragmentListener.View mainView;

    public GetDetailResChatSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
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
    }
}
