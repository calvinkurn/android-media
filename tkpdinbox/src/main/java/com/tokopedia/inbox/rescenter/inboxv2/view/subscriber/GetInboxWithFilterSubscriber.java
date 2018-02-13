package com.tokopedia.inbox.rescenter.inboxv2.view.subscriber;

import android.content.Context;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;

import rx.Subscriber;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxWithFilterSubscriber extends Subscriber<InboxItemResultViewModel> {

    private Context context;
    private ResoInboxFragmentListener.View mainView;

    public GetInboxWithFilterSubscriber(Context context, ResoInboxFragmentListener.View mainView) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.dismissSwipeToRefresh();
        mainView.onErrorGetInboxWithFilter(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxItemResultViewModel inboxItemResultViewModel) {
        mainView.dismissSwipeToRefresh();
        if (inboxItemResultViewModel.getFilterViewModels() != null) {
            inboxItemResultViewModel.setFilterListViewModel(
                    GetInboxSubscriber.convertListToModel(inboxItemResultViewModel.getFilterViewModels()));
        }
        if (inboxItemResultViewModel.getInboxItemViewModels()!=null
                && inboxItemResultViewModel.getInboxItemViewModels().size() != 0) {
            inboxItemResultViewModel.setInboxVisitableList(
                    GetInboxSubscriber.convertModelListToVisitableList(inboxItemResultViewModel.getInboxItemViewModels()));
            mainView.onSuccessGetInboxWithFilter(inboxItemResultViewModel);
        } else {
            mainView.onEmptyGetInboxWithFilter(inboxItemResultViewModel);
        }


    }
}
