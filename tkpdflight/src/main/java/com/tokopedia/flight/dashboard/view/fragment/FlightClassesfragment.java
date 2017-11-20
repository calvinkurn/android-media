package com.tokopedia.flight.dashboard.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.flight.R;
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
public class FlightClassesfragment extends BaseListV2Fragment<FlightClassViewModel> implements FlightClassesContract.View, BaseListV2Adapter.OnBaseListV2AdapterListener<FlightClassViewModel> {

    private OnFragmentInteractionListener interactionListener;

    @Inject
    FlightClassesPresenter presenter;

    public static FlightClassesfragment newInstance() {
        return new FlightClassesfragment();
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
    protected FlightClassesAdapter getNewAdapter() {
        return new FlightClassesAdapter(this);
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
