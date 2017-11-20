package com.tokopedia.flight.search.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.view.DepartureArrivalHeaderView;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.view.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.model.FlightAirportCombineModel;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchFragment extends BaseListV2Fragment<FlightSearchViewModel> implements FlightSearchView,
        BaseListV2Adapter.OnBaseListV2AdapterListener<FlightSearchViewModel>, FlightSearchAdapter.ListenerOnDetailClicked {
    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final int REQUEST_CODE_SEARCH_FILTER = 1;
    private static final int REQUEST_CODE_SEE_DETAIL_FLIGHT = 2;

    private static final String SAVED_FILTER_MODEL = "svd_filter_model";
    private static final String SAVED_SORT_OPTION = "svd_sort_option";
    private static final String SAVED_STAT_MODEL = "svd_stat_model";
    private static final String SAVED_AIRPORT_COMBINE = "svd_airport_combine";

    private BottomActionView filterAndSortBottomAction;

    private FlightFilterModel flightFilterModel;
    private FlightSearchStatisticModel flightSearchStatisticModel;
    protected FlightSearchPassDataViewModel flightSearchPassDataViewModel;

    int selectedSortOption = FlightSortOption.NO_PREFERENCE;

    private OnFlightSearchFragmentListener onFlightSearchFragmentListener;
    private ArrayList<FlightAirportCombineModel> airportCombineModelList;

    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID);
    }

    @Inject
    public FlightSearchPresenter flightSearchPresenter;
    private boolean filterHasChanged;

    public static FlightSearchFragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flightSearchPassDataViewModel = getArguments().getParcelable(EXTRA_PASS_DATA);

        if (savedInstanceState == null) {
            flightFilterModel = new FlightFilterModel();
            selectedSortOption = FlightSortOption.NO_PREFERENCE;
            flightSearchStatisticModel = null;
            setUpCombinationAirport();
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION);
            flightSearchStatisticModel = savedInstanceState.getParcelable(SAVED_STAT_MODEL);
            airportCombineModelList = savedInstanceState.getParcelableArrayList(SAVED_AIRPORT_COMBINE);
        }

    }


    private void setUpCombinationAirport() {
        List<String> departureAirportList;
        String depAirportID = getDepartureAirport().getAirportId();
        if (TextUtils.isEmpty(depAirportID)) {
            String depAirportIDString = getDepartureAirport().getAirportIds();
            String[] depAirportIDs = depAirportIDString.split(",");
            departureAirportList = Arrays.asList(depAirportIDs);
        } else {
            departureAirportList = new ArrayList<>();
            departureAirportList.add(depAirportID);
        }

        List<String> arrivalAirportList;
        String arrAirportID = getArrivalAirport().getAirportId();
        if (TextUtils.isEmpty(arrAirportID)) {
            String arrAirportIDString = getArrivalAirport().getAirportIds();
            String[] arrAirportIDs = arrAirportIDString.split(",");
            arrivalAirportList = Arrays.asList(arrAirportIDs);
        } else {
            arrivalAirportList = new ArrayList<>();
            arrivalAirportList.add(arrAirportID);
        }

        airportCombineModelList = new ArrayList<>();
        for (int i = 0, sizei = departureAirportList.size(); i<sizei; i++) {
            for (int j = 0, sizej = arrivalAirportList.size(); j<sizej; j++) {
                airportCombineModelList.add(
                        new FlightAirportCombineModel(departureAirportList.get(i),
                                arrivalAirportList.get(j)));
            }
        }
    }

    protected FlightAirportDB getDepartureAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    protected FlightAirportDB getArrivalAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
    }

    @Override
    public void onDetailClicked(FlightSearchViewModel flightSearchViewModel) {
        this.startActivityForResult(FlightDetailActivity.createIntent(getActivity(), flightSearchViewModel),
                REQUEST_CODE_SEE_DETAIL_FLIGHT);
    }

    @Override
    protected final void initInjector() {
        DaggerFlightSearchComponent.builder()
                .flightComponent(((FlightModuleRouter) getActivity().getApplication()).getFlightComponent())
                .build()
                .inject(this);
        flightSearchPresenter.attachView(this);
    }

    @Override
    protected final BaseListV2Adapter<FlightSearchViewModel> getNewAdapter() {
        FlightSearchAdapter flightSearchAdapter = new FlightSearchAdapter(this);
        flightSearchAdapter.setListenerOnDetailClicked(this);
        return flightSearchAdapter;
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_flight, container, false);
    }

    protected boolean isReturning() {
        return false;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        flightSearchPresenter.attachView(this);
        if (filterHasChanged) {
            reloadDataFromCache();
            setUIMarkFilter();
            filterHasChanged = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(flightSearchViewModel.getId());
        }
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        // load all data from server
        showLoading();
        String date = isReturning()? flightSearchPassDataViewModel.getReturnDate():
                flightSearchPassDataViewModel.getDepartureDate();
        FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
        int adult = flightPassengerViewModel.getAdult();
        int child = flightPassengerViewModel.getChildren();
        int infant = flightPassengerViewModel.getInfant();
        int classID = flightSearchPassDataViewModel.getFlightClass().getId();
        for (int i = 0, sizei = airportCombineModelList.size(); i<sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.get(i);
            FlightSearchApiRequestModel flightSearchApiRequestModel = new FlightSearchApiRequestModel(
                    flightAirportCombineModel.getDepAirport(),flightAirportCombineModel.getArrAirport(),
                    date, adult, child, infant,classID);
            flightSearchPresenter.searchAndSortFlight(flightSearchApiRequestModel,
                    isReturning(), false, flightFilterModel, selectedSortOption);
        }
    }

    public void reloadDataFromCache() {
        showLoading();
        flightSearchPresenter.searchAndSortFlight(null,
                isReturning(), true, flightFilterModel, selectedSortOption);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpBottomAction(view);

        setUpCombinationAirport();
    }

    protected void setUpBottomAction(View view) {
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addTitleItem(getString(R.string.flight_search_sort_title));

                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selectedSortOption == FlightSortOption.EARLIEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selectedSortOption == FlightSortOption.LATEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selectedSortOption == FlightSortOption.SHORTEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selectedSortOption == FlightSortOption.LONGEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selectedSortOption == FlightSortOption.EARLIEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selectedSortOption == FlightSortOption.LATEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selectedSortOption == FlightSortOption.CHEAPEST);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selectedSortOption == FlightSortOption.MOST_EXPENSIVE);

                BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @SuppressWarnings("WrongConstant")
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                if (getAdapter().getData() != null) {
                                    flightSearchPresenter.sortFlight(getAdapter().getData(), item.getItemId());
                                }
                            }
                        })
                        .createDialog();
                bottomSheetDialog.show();
            }
        });

        setUIMarkSort();
        setUIMarkFilter();

        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightSearchPresenter.getFlightStatistic(isReturning());
            }
        });
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

//    protected void setUpDepArrHeader(View view) {
//        DepartureArrivalHeaderView departureArrivalHeaderView = (DepartureArrivalHeaderView) view.findViewById(R.id.dep_arr_header_view);
//
//        String depAirportID = getDepartureAirport().getAirportId();
//        String depCityCode = getDepartureAirport().getCityCode();
//        String depCityName = getDepartureAirport().getCityName();
//        String departureAirportIdOrCityCode = TextUtils.isEmpty(depAirportID) ? depCityCode : depAirportID;
//
//        String arrAirportID = getArrivalAirport().getAirportId();
//        String arrCityCode = getArrivalAirport().getCityCode();
//        String arrCityName = getArrivalAirport().getCityName();
//        String arrivalAirportIdOrCityCode = TextUtils.isEmpty(arrAirportID) ? arrCityCode : arrAirportID;
//
//        departureArrivalHeaderView.setDeparture(departureAirportIdOrCityCode, depCityName);
//        departureArrivalHeaderView.setArrival(arrivalAirportIdOrCityCode, arrCityName);
//
//    }


    private void setUIMarkFilter() {
        if (flightFilterModel.hasFilter(flightSearchStatisticModel)) {
            filterAndSortBottomAction.setMarkLeft(true);
            getAdapter().setInFilterMode(true);
        } else {
            filterAndSortBottomAction.setMarkLeft(false);
            getAdapter().setInFilterMode(false);
        }
    }

    private void setUIMarkSort() {
        if (selectedSortOption == FlightSortOption.NO_PREFERENCE) {
            filterAndSortBottomAction.setMarkRight(false);
        } else {
            filterAndSortBottomAction.setMarkRight(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_FILTER:
                    if (data != null && data.hasExtra(FlightSearchFilterActivity.EXTRA_FILTER_MODEL)) {
                        flightFilterModel = (FlightFilterModel) data.getExtras().get(FlightSearchFilterActivity.EXTRA_FILTER_MODEL);
                        filterHasChanged = true;
                    }
                    break;
                case REQUEST_CODE_SEE_DETAIL_FLIGHT:
                    if (data != null && data.hasExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED)) {
                        String selectedId = data.getStringExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED);
                        if (!TextUtils.isEmpty(selectedId)) {
                            onFlightSearchFragmentListener.selectFlight(selectedId);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onErrorGetDetailFlightDeparture(Throwable e) {
        // do nothing
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel) {
        // do nothing
    }

    @Override
    public void onSuccessGetDataFromCache(List<FlightSearchViewModel> flightSearchViewModelList) {
        hideLoading();
        getAdapter().clearData();
        getAdapter().addData(flightSearchViewModelList, flightSearchViewModelList.size());
    }

    @Override
    public void onSuccessGetDataFromCloud(List<FlightSearchViewModel> flightSearchViewModelList, FlightMetaDataDB flightMetaDataDB) {
        // TODO check meta data update the list
        // TODO check if the data is empty, but there is data need to fetch, then keep the loading state
        hideLoading();
        getAdapter().addData(flightSearchViewModelList);
        if (getAdapter().getDataSize() > 0 && filterAndSortBottomAction.getVisibility() == View.GONE) {
            filterAndSortBottomAction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSelectedSortItem(int itemId) {
        selectedSortOption = itemId;
        setUIMarkSort();
    }

    @Override
    public void showSortRouteLoading() {
        showLoading();
    }

    @Override
    public void hideSortRouteLoading() {
        hideLoading();
    }

    @Override
    public void onSuccessGetStatistic(FlightSearchStatisticModel flightSearchStatisticModel) {
        this.flightSearchStatisticModel = flightSearchStatisticModel;
        startActivityForResult(FlightSearchFilterActivity.createInstance(getActivity(),
                isReturning(),
                flightSearchStatisticModel,
                flightFilterModel),
                REQUEST_CODE_SEARCH_FILTER);
    }

    @Override
    public void onErrorGetFlightStatistic(Throwable throwable) {
        String message = throwable.getMessage();
        if (!TextUtils.isEmpty(message)) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flightSearchPresenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel);
        outState.putInt(SAVED_SORT_OPTION, selectedSortOption);
        outState.putParcelable(SAVED_STAT_MODEL, flightSearchStatisticModel);
        outState.putParcelableArrayList(SAVED_AIRPORT_COMBINE, airportCombineModelList);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context;
    }
}
