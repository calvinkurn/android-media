package com.tokopedia.flight.dashboard.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.adapter.FlightClassesAdapter;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.presenter.FlightClassesContract;
import com.tokopedia.flight.dashboard.view.presenter.FlightClassesPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightClassesfragment extends BaseListFragment<FlightClassViewModel> implements FlightClassesContract.View, BaseListAdapter.OnBaseListV2AdapterListener<FlightClassViewModel> {
    public static final String EXTRA_FLIGHT_SELECTED_CLASS = "EXTRA_FLIGHT_SELECTED_CLASS";

    private OnFragmentInteractionListener interactionListener;
    private int selectedId;

    @Inject
    FlightClassesPresenter presenter;

    public static FlightClassesfragment newInstance(int selectedId) {
        FlightClassesfragment fragment = new FlightClassesfragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FLIGHT_SELECTED_CLASS, selectedId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showFetchClassesLoading() {
        showLoading();
    }

    @Override
    public void hideFetchClassesLoading() {
        hideLoading();
    }

    @Override
    public void renderFlightClasses(List<FlightClassViewModel> viewModels) {
        onSearchLoaded(viewModels, viewModels.size());
    }

    public interface OnFragmentInteractionListener {
        void actionClassSelected(FlightClassViewModel flightClassViewModel);
    }

    public FlightClassesfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        selectedId = getArguments().getInt(EXTRA_FLIGHT_SELECTED_CLASS, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected FlightClassesAdapter getNewAdapter() {
        FlightClassesAdapter adapter = new FlightClassesAdapter(getContext(), this);
        adapter.setSelectedId(selectedId);
        return adapter;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightDashboardComponent.class).inject(this);
    }

    @Override
    public void onItemClicked(FlightClassViewModel flightClassViewModel) {
        if (interactionListener != null) {
            interactionListener.actionClassSelected(flightClassViewModel);
        }
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        presenter.attachView(this);
        presenter.actionFetchClasses();
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement OnFragmentInteractionListener");
        }
    }
}
