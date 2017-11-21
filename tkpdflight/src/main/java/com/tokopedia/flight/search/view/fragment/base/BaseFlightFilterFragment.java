package com.tokopedia.flight.search.view.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightBaseFilterListener;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;

/**
 * Created by User on 11/17/2017.
 */

public abstract class BaseFlightFilterFragment<T extends ItemType> extends BaseListFragment<T>
    implements OnFlightBaseFilterListener {
    protected OnFlightFilterListener listener;
    private FlightFilterModel originalFilterModel;

    public static final String SAVED_ORIGINAL_FILTER = "svd_ori_filter";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            originalFilterModel = listener.getFlightFilterModel().copy();
        } else {
            originalFilterModel = savedInstanceState.getParcelable(SAVED_ORIGINAL_FILTER);
        }
    }

    @Override
    protected final void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_filter_general, container, false);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        return recyclerView;
    }

    @Override
    protected final String getScreenName() {
        return null;
    }

    @Override
    protected final void initInjector() {
        // no inject
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORIGINAL_FILTER, originalFilterModel);
    }

    @Override
    public void changeFilterToOriginal() {
        listener.onFilterModelChanged(originalFilterModel);
    }
}
