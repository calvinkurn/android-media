package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.search.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchFragment extends BaseListFragment<FlightSearchViewModel> implements FlightSearchView {

    @Inject
    public FlightSearchPresenter flightSearchPresenter;
    private FloatingActionButton filterButton;

    public static FlightSearchFragment newInstance() {
        Bundle args = new Bundle();
        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected final void initInjector() {
        DaggerFlightSearchComponent.builder()
                .flightComponent(((FlightModuleRouter)getActivity().getApplication()).getFlightComponent() )
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
        flightSearchPresenter.searchDepartureFlight(false);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //TODO this fab just for testing only
        filterButton = (FloatingActionButton) view.findViewById(R.id.fab);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getData() == null || adapter.getData().size() == 0) {
                    return;
                }
                FlightSearchFilterActivity.start(getContext(), isReturning(),
                        new FlightSearchStatisticModel(adapter.getData()),new FlightFilterModel());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    if (!filterButton.isShown()) {
                        filterButton.show();
                    }
                } else {
                    if (filterButton.isShown()) {
                        filterButton.hide();
                    }
                }
            }
        });
        return view;
    }

    protected boolean isReturning(){
        return false;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
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
}
