package com.tokopedia.flight.orderlist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightOrderListPresenter;

import javax.inject.Inject;

/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseDaggerFragment implements FlightOrderListContract.View {
    private RecyclerView ordersRecyclerView;

    @Inject
    FlightOrderListPresenter presenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
    }

    public static FlightOrderListFragment createInstance() {
        return new FlightOrderListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_order_list, container, false);
        ordersRecyclerView = view.findViewById(R.id.rv_orders);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getInitialOrderData();
    }

    @Override
    public void showGetInitialOrderDataLoading() {

    }

    @Override
    public void hideGetInitialOrderDataLoading() {

    }
}
