package com.tokopedia.flight.search.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener2;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightSearchView2 extends BaseListViewListener2<FlightSearchViewModel> {

    void setSelectedSortItem(int itemId);

    void showSortRouteLoading();

    void hideSortRouteLoading();

    void onSuccessGetStatistic(FlightSearchStatisticModel flightSearchStatisticModel);

    void onSuccessGetDataFromCache(List<FlightSearchViewModel> flightSearchViewModelList);

    void onSuccessGetDataFromCloud(boolean isDataEmpty, FlightMetaDataDB flightMetaDataDB);

    void onErrorDeleteFlightCache(Throwable throwable);

    void onSuccessDeleteFlightCache();

    void onErrorGetFlightStatistic(Throwable throwable);

    void onErrorGetDetailFlightDeparture(Throwable e);

    void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel);

    void hideHorizontalProgress();

    void removeToolbarElevation();

    void addToolbarElevation();

    Activity getActivity();
}
