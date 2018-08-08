package com.tokopedia.inbox.rescenter.inboxv2.view.subscriber;

import android.content.Context;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SingleItemInboxResultViewModel;

import rx.Subscriber;

/**
 * Created by yfsx on 01/02/18.
 */

public class GetSingleItemSubscriber extends Subscriber<SingleItemInboxResultViewModel> {

    private Context context;
    private ResoInboxFragmentListener.View mainView;

    public GetSingleItemSubscriber (Context context, ResoInboxFragmentListener.View mainView) {
        this.context = context;
        this.mainView = mainView;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissProgressBar();
        mainView.onErrorGetSingleInboxItem(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SingleItemInboxResultViewModel model) {
        if (model != null) {
            model.setFilterListViewModel(GetInboxSubscriber.convertListToModel(model.getFilterViewModels()));
            mainView.onSuccessGetSingleInboxItem(model);
        } else {
            mainView.dismissProgressBar();
        }
    }
}
