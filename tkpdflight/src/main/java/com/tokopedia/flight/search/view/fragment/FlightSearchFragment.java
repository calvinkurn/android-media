package com.tokopedia.flight.search.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.search.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchFragment extends BaseListFragment<FlightSearchViewModel> implements FlightSearchView {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final int REQUEST_CODE_SEARCH_FILTER = 1;

    private static final String SAVED_FILTER_MODEL = "svd_filter_model";
    private static final String SAVED_STAT_MODEL = "svd_stat_model";

    BottomActionView filterAndSortBottomAction;

    private FlightFilterModel flightFilterModel;
    private FlightSearchStatisticModel flightSearchStatisticModel;

    //TODO changeSelected with some Param
    int selected = 0;

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
        if (savedInstanceState == null) {
            flightFilterModel = new FlightFilterModel();
            flightSearchStatisticModel = null;
        } else {
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            flightSearchStatisticModel = savedInstanceState.getParcelable(SAVED_STAT_MODEL);
        }
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
        return new FlightSearchAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        flightSearchPresenter.searchFlight(isReturning(), false, flightFilterModel);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    if (!filterAndSortBottomAction.isShown()) {
                        filterAndSortBottomAction.show();
                    }
                } else {
                    if (filterAndSortBottomAction.isShown()) {
                        filterAndSortBottomAction.hide();
                    }
                }
            }
        });
        return view;
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
            currentPage = getStartPage();
            adapter.clearData();
            adapter.showRetryFull(false);
            adapter.showLoadingFull(true);
            flightSearchPresenter.searchFlight(isReturning(), true, flightFilterModel);
            filterHasChanged = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_search_flight;
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {
        getActivity().startActivity(FlightDetailActivity.createIntent(getActivity(), flightSearchViewModel));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addTitleItem(getString(R.string.flight_search_sort_title));

                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selected == FlightSortOption.EARLIEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selected == FlightSortOption.LATEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selected == FlightSortOption.SHORTEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selected == FlightSortOption.LONGEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selected == FlightSortOption.EARLIEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selected == FlightSortOption.LATEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selected == FlightSortOption.CHEAPEST);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selected == FlightSortOption.MOST_EXPENSIVE);

                BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                flightSearchPresenter.onSortItemSelected(item.getItemId());
                            }
                        })
                        .createDialog();
                bottomSheetDialog.show();
            }
        });

        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getData() == null || adapter.getData().size() == 0) {
                    return;
                }
                startActivityForResult(FlightSearchFilterActivity.createInstance(getActivity(),
                        isReturning(),
                        flightSearchStatisticModel,
                        flightFilterModel),
                        REQUEST_CODE_SEARCH_FILTER);
            }
        });
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
            }
        }
    }

    @Override
    public void onSearchLoaded(@NonNull List<FlightSearchViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        // TODO, will not work if api has paging, will be converted to usecase later
        if (totalItem > 0 && flightSearchStatisticModel == null) {
            flightSearchStatisticModel = new FlightSearchStatisticModel(list);
        }
    }

    @Override
    public RequestParams getSearchFlightRequestParam() {
        return FlightSearchUseCase.generateRequestParams(false, true, flightFilterModel);
    }

    @Override
    public void setSelectedSortItem(int itemId) {
        selected = itemId;
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
    public void onDestroy() {
        super.onDestroy();
        flightSearchPresenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel);
        outState.putParcelable(SAVED_STAT_MODEL, flightSearchStatisticModel);
    }
}
