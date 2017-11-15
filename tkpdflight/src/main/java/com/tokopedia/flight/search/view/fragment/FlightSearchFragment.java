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
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.search.view.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

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

    BottomActionView filterAndSortBottomAction;
    protected TextView departureAirportCode;
    protected TextView departureAirportName;
    protected TextView arrivalAirportCode;
    protected TextView arrivalAirportName;

    private FlightFilterModel flightFilterModel;
    protected FlightSearchPassDataViewModel flightSearchPassDataViewModel;

    int selectedSortOption = FlightSortOption.NO_PREFERENCE;

    private OnFlightSearchFragmentListener onFlightSearchFragmentListener;

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
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION);
        }
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
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return null;
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
            loadInitialData();
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

    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        showLoading();
        flightSearchPresenter.searchAndSortFlight(flightSearchPassDataViewModel,
                isReturning(), false, flightFilterModel, selectedSortOption);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        departureAirportCode = (TextView) view.findViewById(R.id.departure_airport_code);
        departureAirportName = (TextView) view.findViewById(R.id.departure_airport_name);
        arrivalAirportCode = (TextView) view.findViewById(R.id.arrival_airport_code);
        arrivalAirportName = (TextView) view.findViewById(R.id.arrival_airport_name);

        String departureAirportId = TextUtils.isEmpty(flightSearchPassDataViewModel.getDepartureAirport().getAirportId()) ?
                flightSearchPassDataViewModel.getDepartureAirport().getCityId() : flightSearchPassDataViewModel.getDepartureAirport().getAirportId();
        departureAirportCode.setText(departureAirportId);
        departureAirportName.setText(flightSearchPassDataViewModel.getDepartureAirport().getCityName());
        String arrivalAirportId = TextUtils.isEmpty(flightSearchPassDataViewModel.getArrivalAirport().getAirportId()) ?
                flightSearchPassDataViewModel.getArrivalAirport().getCityId() : flightSearchPassDataViewModel.getArrivalAirport().getAirportId();
        arrivalAirportCode.setText(arrivalAirportId);
        arrivalAirportName.setText(flightSearchPassDataViewModel.getArrivalAirport().getCityName());
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
                                if (getAdapter().getData() == null) {
                                    flightSearchPresenter.searchAndSortFlight(flightSearchPassDataViewModel,
                                            isReturning(), true, flightFilterModel, item.getItemId());
                                } else {
                                    flightSearchPresenter.sortFlight(getAdapter().getData(), item.getItemId());
                                }
                            }
                        })
                        .createDialog();
                bottomSheetDialog.show();
            }
        });

        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightSearchPresenter.getFlightStatistic(isReturning());
            }
        });
        filterAndSortBottomAction.setVisibility(View.GONE);
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
    public void onSearchLoaded(@NonNull List<FlightSearchViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (filterAndSortBottomAction.getVisibility() == View.GONE) {
            filterAndSortBottomAction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSelectedSortItem(int itemId) {
        selectedSortOption = itemId;
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
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context;
    }
}
