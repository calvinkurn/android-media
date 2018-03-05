package com.tokopedia.inbox.rescenter.inboxv2.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

import java.util.ArrayList;
import java.util.List;

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
        mainView.dismissSwipeToRefresh();
        mainView.onErrorGetInbox(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxItemResultViewModel inboxItemResultViewModel) {
        mainView.dismissSwipeToRefresh();
        if (inboxItemResultViewModel.getFilterViewModels() != null) {
            inboxItemResultViewModel.setFilterListViewModel(
                    convertListToModel(inboxItemResultViewModel.getFilterViewModels()));
        }
        if (inboxItemResultViewModel.getInboxItemViewModels() != null &&
                inboxItemResultViewModel.getInboxItemViewModels().size() != 0) {
            inboxItemResultViewModel.setInboxVisitableList(
                    convertModelListToVisitableList(inboxItemResultViewModel.getInboxItemViewModels()));
            mainView.onSuccessGetInbox(inboxItemResultViewModel);
        } else {
            mainView.onEmptyGetInbox(inboxItemResultViewModel);
        }
    }

    public static List<Visitable> convertModelListToVisitableList(List<InboxItemViewModel> models) {
        List<Visitable> targetList = new ArrayList<>();
        targetList.addAll(models);
        return targetList;
    }

    public static FilterListViewModel convertListToModel(List<FilterViewModel> models) {
        return new FilterListViewModel(models);
    }
}
