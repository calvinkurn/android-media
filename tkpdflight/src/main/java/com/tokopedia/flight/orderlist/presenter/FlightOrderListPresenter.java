package com.tokopedia.flight.orderlist.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;
import com.tokopedia.flight.orderlist.domain.FlightSendEmailUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.view.viewmodel.mapper.FlightOrderViewModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 12/6/17.
 */

public class FlightOrderListPresenter extends BaseDaggerPresenter<FlightOrderListContract.View>
        implements FlightOrderListContract.Presenter {
    private UserSession userSession;
    private FlightGetOrdersUseCase flightGetOrdersUseCase;
    private FlightOrderViewModelMapper flightOrderViewModelMapper;
    private CompositeSubscription compositeSubscription;

    private String userResendEmail = "";

    @Inject
    public FlightOrderListPresenter(UserSession userSession,
                                    FlightGetOrdersUseCase flightGetOrdersUseCase,
                                    FlightOrderViewModelMapper flightOrderViewModelMapper) {
        this.userSession = userSession;
        this.flightGetOrdersUseCase = flightGetOrdersUseCase;
        this.flightOrderViewModelMapper = flightOrderViewModelMapper;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadData(String selectedFilter, final int page, final int perPage) {
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(page - 1 >= 0 ? page - 1 : page, selectedFilter, perPage), new Subscriber<List<FlightOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<FlightOrder> orderEntities) {
                List<Visitable> visitables = flightOrderViewModelMapper.transform(orderEntities);
                if (page == 1) {
                    buildAndRenderFilterList();
                }
                getView().renderList(visitables, visitables.size() >= perPage);
            }
        });
    }

    @Override
    public void onDestroyView() {
        detachView();
        flightGetOrdersUseCase.unsubscribe();
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDownloadEticket(String invoiceId) {
        getView().navigateToInputEmailForm(invoiceId, userSession.getUserId(), userResendEmail);
    }

    private void buildAndRenderFilterList() {
        int[] colorBorder = new int[6];
        colorBorder[0] = R.color.tkpd_main_green;
        colorBorder[1] = R.color.tkpd_main_green;
        colorBorder[2] = R.color.tkpd_main_green;
        colorBorder[3] = R.color.tkpd_main_green;
        colorBorder[4] = R.color.tkpd_main_green;
        colorBorder[5] = R.color.tkpd_main_green;

        List<SimpleViewModel> filtersMap = new ArrayList<>();
        filtersMap.add(new SimpleViewModel("", getView().getString(R.string.flight_order_status_all_label)));
        filtersMap.add(new SimpleViewModel("700,800", getView().getString(R.string.flight_order_status_success_label)));
        filtersMap.add(new SimpleViewModel("600", getView().getString(R.string.flight_order_status_failed_label)));
        filtersMap.add(new SimpleViewModel("102,101", getView().getString(R.string.flight_order_status_waiting_for_payment_label)));
        filtersMap.add(new SimpleViewModel("200,300", getView().getString(R.string.flight_order_status_in_progress_label)));
        filtersMap.add(new SimpleViewModel("650", getView().getString(R.string.flight_order_status_refund_label)));

        List<QuickFilterItem> filterItems = new ArrayList<>();
        boolean isAnyItemSelected = false;
        for (SimpleViewModel entry : filtersMap) {
            QuickFilterItem finishFilter = new QuickFilterItem();
            finishFilter.setName(entry.getDescription());
            finishFilter.setType(entry.getLabel());
            finishFilter.setColorBorder(R.color.tkpd_main_green);
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getLabel())) {
                isAnyItemSelected = true;
                finishFilter.setSelected(true);
            } else {
                finishFilter.setSelected(false);
            }
            filterItems.add(finishFilter);
        }

        if (!isAnyItemSelected && filterItems.size() > 0) {
            filterItems.get(0).setSelected(true);
        }

        getView().renderOrderStatus(filterItems);

    }

    @Override
    public void onGetProfileData() {
        compositeSubscription.add(getView().getProfileObservable()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProfileInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ProfileInfo profileInfo) {
                        if (profileInfo != null && isViewAttached()) {
                            userResendEmail = profileInfo.getEmail();
                        }
                    }
                })
        );
    }
}
