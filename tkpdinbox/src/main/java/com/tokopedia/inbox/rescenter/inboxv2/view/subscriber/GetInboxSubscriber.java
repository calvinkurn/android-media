package com.tokopedia.inbox.rescenter.inboxv2.view.subscriber;

import android.content.Context;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;

import rx.Subscriber;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxSubscriber extends Subscriber<InboxItemResultViewModel> {

    private Context context;
    private ResoInboxFragmentListener.View mainView;

    public GetInboxSubscriber(Context context, ResoInboxFragmentListener.View mainView) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.onErrorGetInbox(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxItemResultViewModel inboxItemResultViewModel) {
        mainView.onSuccessGetInbox(inboxItemResultViewModel);
    }
}
