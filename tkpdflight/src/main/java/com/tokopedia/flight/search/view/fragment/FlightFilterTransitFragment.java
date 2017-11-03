package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.utils.CommonUtils;
import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionLabelView;
import com.tokopedia.design.label.selection.text.SelectionTextLabelView;
import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.design.text.DecimalRangeInputView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightFilterTransitFragment extends BaseListFragment<FlightSearchViewModel> {
    public static final String TAG = FlightFilterTransitFragment.class.getSimpleName();

    private OnFlightFilterListener listener;

    public static FlightFilterTransitFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterTransitFragment fragment = new FlightFilterTransitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnFilterTransitFragmentListener onFilterFragmentListener;
    public interface OnFilterTransitFragmentListener{
        FlightSearchStatisticModel getFlightSearchStatisticModel();
        FlightFilterModel getFlightFilterModel();
        void onFilterModelChanged(FlightFilterModel flightFilterModel);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        FlightFilterModel flightFilterModel= listener.getFlightFilterModel();

        return view;
    }

    @Override
    public void onSearchLoaded(@NonNull List<FlightSearchViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);

    }

    protected int getFragmentLayout() {
        return R.layout.fragment_flight_filter_transit;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<FlightSearchViewModel> getNewAdapter() {
        return new FlightSearchAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }

}
