package com.tokopedia.flight.dashboard.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.adapter.FlightClassesAdapterTypeFactory;
import com.tokopedia.flight.dashboard.view.adapter.viewholder.FlightClassViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.presenter.FlightClassesContract;
import com.tokopedia.flight.dashboard.view.presenter.FlightClassesPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightClassesfragment extends BaseListFragment<FlightClassViewModel, FlightClassesAdapterTypeFactory> implements FlightClassesContract.View, FlightClassViewHolder.ListenerCheckedClass {
    public static final String EXTRA_FLIGHT_SELECTED_CLASS = "EXTRA_FLIGHT_SELECTED_CLASS";
    @Inject
    FlightClassesPresenter presenter;
    private OnFragmentInteractionListener interactionListener;
    private int selectedId;

    public FlightClassesfragment() {
        // Required empty public constructor
    }

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
        renderList(viewModels);
    }

    @Override
    public boolean isItemChecked(FlightClassViewModel selectedItem) {
        return selectedItem.getId() == selectedId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        selectedId = getArguments().getInt(EXTRA_FLIGHT_SELECTED_CLASS, -1);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.actionFetchClasses();
    }

    @Override
    protected FlightClassesAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightClassesAdapterTypeFactory(this);
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
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void actionClassSelected(FlightClassViewModel flightClassViewModel);
    }
}
