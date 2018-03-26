package com.tokopedia.flight.orderlist.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.cancellation.domain.mapper.FlightOrderToCancellationJourneyMapper;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 12/6/17.
 */

public interface FlightOrderListContract {

    interface View extends BaseListViewListener<Visitable> {

        void renderOrderStatus(List<QuickFilterItem> filterItems);

        String getString(int resId);

        String getSelectedFilter();

        Context getActivity();

        void navigateToInputEmailForm(String invoiceId, String userId, String userEmail);

        Observable<ProfileInfo> getProfileObservable();
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadData(String selectedFilter, int page, int perPage);

        void onDestroyView();

        void onDownloadEticket(String invoiceId);

        void onGetProfileData();

        List<FlightCancellationJourney> transformOrderToCancellation(FlightOrderJourney flightOrderJourney);
    }
}
