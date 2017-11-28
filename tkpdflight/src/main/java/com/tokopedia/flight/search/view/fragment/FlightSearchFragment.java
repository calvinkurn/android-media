package com.tokopedia.flight.search.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.common.view.HorizontalProgressBar;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.view.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.model.AirportCombineModelList;
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

public class FlightSearchFragment extends BaseListFragment<FlightSearchViewModel> implements FlightSearchView,
        BaseListAdapter.OnBaseListV2AdapterListener<FlightSearchViewModel>,
        FlightSearchAdapter.OnBaseFlightSearchAdapterListener {
    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final int REQUEST_CODE_SEARCH_FILTER = 1;
    private static final int REQUEST_CODE_SEE_DETAIL_FLIGHT = 2;

    private static final String SAVED_FILTER_MODEL = "svd_filter_model";
    private static final String SAVED_SORT_OPTION = "svd_sort_option";
    private static final String SAVED_STAT_MODEL = "svd_stat_model";
    private static final String SAVED_AIRPORT_COMBINE = "svd_airport_combine";
    private static final String SAVED_PROGRESS = "svd_progress";
    public static final int MAX_PROGRESS = 100;

    private BottomActionView filterAndSortBottomAction;

    private FlightFilterModel flightFilterModel;
    private FlightSearchStatisticModel flightSearchStatisticModel;
    protected FlightSearchPassDataViewModel flightSearchPassDataViewModel;

    private HorizontalProgressBar progressBar;
    private int progress;

    int selectedSortOption;

    private OnFlightSearchFragmentListener onFlightSearchFragmentListener;
    private AirportCombineModelList airportCombineModelList;
    private FlightSearchAdapter flightSearchAdapter;

    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID);
    }

    @Inject
    public FlightSearchPresenter flightSearchPresenter;
    private boolean needRefreshFromCache;

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
            selectedSortOption = FlightSortOption.CHEAPEST;
            flightSearchStatisticModel = null;
            setUpCombinationAirport();
            progress = 0;
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION);
            flightSearchStatisticModel = savedInstanceState.getParcelable(SAVED_STAT_MODEL);
            airportCombineModelList = savedInstanceState.getParcelable(SAVED_AIRPORT_COMBINE);
            progress = savedInstanceState.getInt(SAVED_PROGRESS, 0);
            needRefreshFromCache = true;
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

        airportCombineModelList = new AirportCombineModelList(departureAirportList, arrivalAirportList);
    }

    protected FlightAirportDB getDepartureAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    protected FlightAirportDB getArrivalAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
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
    protected final BaseListAdapter<FlightSearchViewModel> getNewAdapter() {
        flightSearchAdapter = new FlightSearchAdapter(getContext(), this, this);
        return flightSearchAdapter;
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        progressBar = (HorizontalProgressBar) view.findViewById(R.id.horizontal_progress_bar);
        setUpProgress();
        setUpBottomAction(view);
        return view;
    }

    protected int getLayout() {
        return R.layout.fragment_search_flight;
    }

    @Override
    protected boolean needLoadDataAtStart() {
        return false; // load data will be handled in onResume Manually
    }

    private void setUpProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            if (progress >= MAX_PROGRESS) {
                progress = MAX_PROGRESS;
                progressBar.setProgress(MAX_PROGRESS);
                flightSearchPresenter.setDelayHorizontalProgress();
            } else {
                progressBar.setProgress(progress);
            }
        }
    }

    protected boolean isReturning() {
        return false;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        flightSearchPresenter.attachView(this);
        if (needRefreshFromCache) {
            reloadDataFromCache();
            setUIMarkFilter();
            needRefreshFromCache = false;
        }
        loadInitialData();
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    @Override
    public void hideHorizontalProgress() {
        progressBar.setVisibility(View.INVISIBLE);
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

    /**
     * load all data from cloud
     */
    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        if (getAdapter().getDataSize() == 0) {
            showLoading();
        }
        String date = flightSearchPassDataViewModel.getDate(isReturning());
        FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
        int adult = flightPassengerViewModel.getAdult();
        int child = flightPassengerViewModel.getChildren();
        int infant = flightPassengerViewModel.getInfant();
        int classID = flightSearchPassDataViewModel.getFlightClass().getId();
        boolean anyLoadToCloud = false;
        for (int i = 0, sizei = airportCombineModelList.getData().size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.getData().get(i);
            boolean needLoadFromCloud = true;
            if (flightAirportCombineModel.isHasLoad() && !flightAirportCombineModel.isNeedRefresh()) {
                needLoadFromCloud = false;
            }
            if (needLoadFromCloud) {
                anyLoadToCloud = true;
                FlightSearchApiRequestModel flightSearchApiRequestModel = new FlightSearchApiRequestModel(
                        flightAirportCombineModel.getDepAirport(), flightAirportCombineModel.getArrAirport(),
                        date, adult, child, infant, classID);
                flightSearchPresenter.searchAndSortFlight(flightSearchApiRequestModel,
                        isReturning(), false, flightFilterModel, selectedSortOption);
            }
        }
        if (!anyLoadToCloud) {
            reloadDataFromCache();
        }
    }

    /**
     * load all data from cache
     */
    public void reloadDataFromCache() {
        flightSearchPresenter.searchAndSortFlight(null,
                isReturning(), true, flightFilterModel, selectedSortOption);
    }

    protected void setUpBottomAction(View view) {
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addTitleItem(getString(R.string.flight_search_sort_title));

                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selectedSortOption == FlightSortOption.CHEAPEST);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selectedSortOption == FlightSortOption.MOST_EXPENSIVE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selectedSortOption == FlightSortOption.EARLIEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selectedSortOption == FlightSortOption.LATEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selectedSortOption == FlightSortOption.SHORTEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selectedSortOption == FlightSortOption.LONGEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selectedSortOption == FlightSortOption.EARLIEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selectedSortOption == FlightSortOption.LATEST_ARRIVAL);

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
                        needRefreshFromCache = true;
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
    public void onSuccessGetDataFromCloud(boolean isDataEmpty, FlightMetaDataDB flightMetaDataDB) {
        String depAirport = flightMetaDataDB.getDepartureAirport();
        String arrivalAirport = flightMetaDataDB.getArrivalAirport();
        FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.getData(depAirport, arrivalAirport);
        int size = airportCombineModelList.getData().size();
        int halfProgressAmount = divideTo(divideTo(MAX_PROGRESS, size), 2);
        if (!flightAirportCombineModel.isHasLoad()) {
            flightAirportCombineModel.setHasLoad(true);
            progress += halfProgressAmount;
        }

        if (flightAirportCombineModel.isNeedRefresh()) {
            if (flightMetaDataDB.isNeedRefresh()) {
                int noRetry = flightAirportCombineModel.getNoOfRetry();
                noRetry++;
                flightAirportCombineModel.setNoOfRetry(noRetry);
                progress += divideTo(halfProgressAmount, flightMetaDataDB.getMaxRetry());
                // already reach max retry limit, end retrying.
                if (noRetry >= flightMetaDataDB.getMaxRetry()) {
                    flightAirportCombineModel.setNeedRefresh(false);
                } else { //no retry still below the max retry, do retry
                    //retry load data
                    String date = flightSearchPassDataViewModel.getDate(isReturning());
                    FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
                    int adult = flightPassengerViewModel.getAdult();
                    int child = flightPassengerViewModel.getChildren();
                    int infant = flightPassengerViewModel.getInfant();
                    int classID = flightSearchPassDataViewModel.getFlightClass().getId();
                    FlightSearchApiRequestModel flightSearchApiRequestModel = new FlightSearchApiRequestModel(
                            flightAirportCombineModel.getDepAirport(), flightAirportCombineModel.getArrAirport(),
                            date, adult, child, infant, classID);
                    flightSearchPresenter.searchAndSortFlightWithDelay(flightSearchApiRequestModel, isReturning(), flightMetaDataDB.getRefreshTime());
                }

            } else {
                flightAirportCombineModel.setNeedRefresh(false);
                progress += (flightMetaDataDB.getMaxRetry() - flightAirportCombineModel.getNoOfRetry()) *
                        divideTo(halfProgressAmount, flightMetaDataDB.getMaxRetry());
            }
        }
        setUpProgress();

        // if the data is empty, but there is data need to fetch, then keep the loading state
        if (getAdapter().getDataSize() == 0 && isDataEmpty &&
                airportCombineModelList.isRetrievingData()) {
            return;
        }

        // will update the data
        // if there is already data loaded, reload the data from cache
        // because the data might have filter/sort in it, so cannot be added directly
        if (isDataEmpty) {
            hideLoading();
        } else {
            // we retrieve from cache, because there is possibility the filter/sort will be different
            reloadDataFromCache();
            if (filterAndSortBottomAction.getVisibility() == View.GONE) {
                filterAndSortBottomAction.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        super.onLoadSearchError(t);
        String message = FlightErrorUtil.getMessageFromException(t);
        flightSearchAdapter.setErrorMessage(message);
    }

    private int divideTo(int number, int pieces) {
        return (int) ((number / pieces) + 0.5);
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
    public void onDetailClicked(FlightSearchViewModel flightSearchViewModel) {
        FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel();
        flightDetailViewModel.build(flightSearchViewModel);
        flightDetailViewModel.build(flightSearchPassDataViewModel);
        this.startActivityForResult(FlightDetailActivity.createIntent(getActivity(), flightDetailViewModel),
                REQUEST_CODE_SEE_DETAIL_FLIGHT);
    }

    @Override
    public void onResetFilterClicked() {
        flightFilterModel = new FlightFilterModel();
        showLoading();
        setUIMarkFilter();
        reloadDataFromCache();
    }

    @Override
    public void onChangeDateClicked() {
//        String dateInput = flightSearchPassDataViewModel.getDate(isReturning());
//        Date date = FlightDateUtil.stringToDate(dateInput);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                //presenter.onReturnDateChange(year, month, dayOfMonth);
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
//        DatePicker datePicker1 = datePicker.getDatePicker();
//        if (isReturning()) {
//            String dateDepStr = flightSearchPassDataViewModel.getDate(false);
//            Date dateDep = FlightDateUtil.stringToDate(dateDepStr);
//            datePicker1.setMinDate(dateDep.getTime());
//        } else {
//
//        }
//
//        datePicker.show();
    }

    @Override
    public void onErrorGetFlightStatistic(Throwable throwable) {
        String message = FlightErrorUtil.getMessageFromException(throwable);
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
        outState.putParcelable(SAVED_AIRPORT_COMBINE, airportCombineModelList);
        outState.putInt(SAVED_PROGRESS, progress);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context;
    }
}
