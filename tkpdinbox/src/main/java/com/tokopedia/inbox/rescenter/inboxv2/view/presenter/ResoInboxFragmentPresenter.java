package com.tokopedia.inbox.rescenter.inboxv2.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.domain.params.GetInboxParams;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerLoadMoreUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerLoadMoreUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSingleItemUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.subscriber.GetInboxLoadMoreSubscriber;
import com.tokopedia.inbox.rescenter.inboxv2.view.subscriber.GetInboxSubscriber;
import com.tokopedia.inbox.rescenter.inboxv2.view.subscriber.GetInboxWithFilterSubscriber;
import com.tokopedia.inbox.rescenter.inboxv2.view.subscriber.GetSingleItemSubscriber;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragmentPresenter
        extends BaseDaggerPresenter<ResoInboxFragmentListener.View>
        implements ResoInboxFragmentListener.Presenter {

    private Context context;
    private boolean isSeller;
    private ResoInboxSortFilterModel filterModel;

    private GetInboxBuyerUseCase getInboxBuyerUseCase;
    private GetInboxSellerUseCase getInboxSellerUseCase;
    private GetInboxBuyerLoadMoreUseCase getInboxBuyerLoadMoreUseCase;
    private GetInboxSellerLoadMoreUseCase getInboxSellerLoadMoreUseCase;
    private GetInboxSingleItemUseCase getInboxSingleItemUseCase;

    private ResoInboxFragmentListener.View mainView;

    @Inject
    public ResoInboxFragmentPresenter(GetInboxBuyerUseCase getInboxBuyerUseCase,
                                      GetInboxSellerUseCase getInboxSellerUseCase,
                                      GetInboxBuyerLoadMoreUseCase getInboxBuyerLoadMoreUseCase,
                                      GetInboxSellerLoadMoreUseCase getInboxSellerLoadMoreUseCase,
                                      GetInboxSingleItemUseCase getInboxSingleItemUseCase) {
        this.getInboxBuyerUseCase = getInboxBuyerUseCase;
        this.getInboxSellerUseCase = getInboxSellerUseCase;
        this.getInboxSellerLoadMoreUseCase = getInboxSellerLoadMoreUseCase;
        this.getInboxBuyerLoadMoreUseCase = getInboxBuyerLoadMoreUseCase;
        this.getInboxSingleItemUseCase = getInboxSingleItemUseCase;
    }

    @Override
    public void initPresenterData(Context context, boolean isSeller) {
        this.isSeller = isSeller;
        resetPresenterDataAndCallInbox();
    }

    @Override
    public void resetFilterClicked() {
        resetPresenterDataAndCallInbox();
    }

    private void resetPresenterDataAndCallInbox() {
        filterModel = new ResoInboxSortFilterModel();
        getInbox();
    }

    @Override
    public void getInbox() {
        mainView.showProgressBar();
        filterModel.setStartID("");
        getInbox(new GetInboxSubscriber(context, mainView));
    }

    @Override
    public void getInboxResetFilter() {
        filterModel = new ResoInboxSortFilterModel();
        getInbox();
    }

    private void getInbox(Subscriber subscriber) {
        if (isSeller)
            getInboxSellerUseCase.execute(
                    GetInboxParams.getParams(filterModel),
                    subscriber);
        else
            getInboxBuyerUseCase.execute(
                    GetInboxParams.getParams(filterModel),
                    subscriber);
    }

    @Override
    public void getInboxWithParams(ResoInboxSortModel inboxSortModel, ResoInboxFilterModel inboxFilterModel) {
        filterModel.setSortBy(inboxSortModel.getSelectedSortModel().getSortById());
        filterModel.setAsc(inboxSortModel.getSelectedSortModel().getAscId());
        filterModel.setEndDateString(inboxFilterModel.getDateToString());
        filterModel.setStartDateString(inboxFilterModel.getDateFromString());
        filterModel.setFilters(inboxFilterModel.getSelectedFilterList());
        filterModel.setStartID("");
        getInbox(new GetInboxWithFilterSubscriber(context, mainView));
    }

    @Override
    public void getSingleItemInbox(int inboxId) {
        getInboxSingleItemUseCase.execute(
                GetInboxSingleItemUseCase.getParams(inboxId),
                new GetSingleItemSubscriber(context, mainView));
    }

    @Override
    public void loadMoreInbox(String token) {
        filterModel.setStartID(token);
        if (isSeller)
            getInboxSellerLoadMoreUseCase.execute(
                    GetInboxParams.getParams(filterModel),
                    new GetInboxLoadMoreSubscriber(context, mainView));
        else
            getInboxBuyerLoadMoreUseCase.execute(
                    GetInboxParams.getParams(filterModel),
                    new GetInboxLoadMoreSubscriber(context, mainView));
    }

    @Override
    public void quickFilterClicked(int orderValue) {
        if (!filterModel.getFilters().contains(orderValue)) {
            filterModel.getFilters().add(orderValue);
        } else {
            List<Integer> newFilters = new ArrayList<>();
            for (Integer oldValue : filterModel.getFilters()) {
                if (oldValue != orderValue) {
                    newFilters.add(oldValue);
                }
            }
            filterModel.setFilters(newFilters);
        }
        getInbox();
    }

    @Override
    public void attachView(ResoInboxFragmentListener.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getInboxBuyerUseCase.unsubscribe();
        getInboxSellerUseCase.unsubscribe();
        getInboxBuyerLoadMoreUseCase.unsubscribe();
        getInboxSellerLoadMoreUseCase.unsubscribe();
        getInboxSingleItemUseCase.unsubscribe();
    }

}
