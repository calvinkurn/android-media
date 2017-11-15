package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.adapter.FlightFilterAirlineAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightResettableListener;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.AirlineStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FlightFilterAirlineFragment extends BaseListV2Fragment<AirlineStat>
        implements BaseListV2Adapter.OnBaseListV2AdapterListener<AirlineStat>,
        BaseListCheckableV2Adapter.OnCheckableAdapterListener<AirlineStat>,
        OnFlightResettableListener {
    public static final String TAG = FlightFilterAirlineFragment.class.getSimpleName();

    private OnFlightFilterListener listener;
    private FlightFilterAirlineAdapter adapter;

    public static FlightFilterAirlineFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterAirlineFragment fragment = new FlightFilterAirlineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_filter_airline, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        // no inject
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    protected BaseListV2Adapter<AirlineStat> getNewAdapter() {
        adapter = new FlightFilterAirlineAdapter(this, this);
        return adapter;
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


    @Override
    protected void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }

    @Override
    public void onItemClicked(AirlineStat airlineStat) {
        // no op
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<AirlineStat> airlineStatList = listener.getFlightSearchStatisticModel().getAirlineStatList();
        onSearchLoaded(airlineStatList, airlineStatList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<AirlineStat> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<String> airlineList = flightFilterModel.getAirlineList();
            if (airlineList!= null) {
                for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
                    String selectedAirline = airlineList.get(i);
                    List<AirlineStat> airlineStatList = adapter.getData();
                    if (airlineStatList != null) {
                        for (int j = 0, sizej = airlineStatList.size(); j < sizej; j++) {
                            AirlineStat airlineStat = airlineStatList.get(j);
                            if (airlineStat.getAirlineDB().getId().equals(selectedAirline)) {
                                checkedPositionList.add(j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        adapter.setCheckedPositionList(checkedPositionList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(AirlineStat airlineStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<AirlineStat> airlineStatList = adapter.getCheckedDataList();
        List<String> airlineList = Observable.from(airlineStatList).map(new Func1<AirlineStat, String>() {
            @Override
            public String call(AirlineStat airlineStat) {
                return airlineStat.getAirlineDB().getId();
            }
        }).toList().toBlocking().first();
        flightFilterModel.setAirlineList(airlineList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void reset() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setAirlineList(new ArrayList<String>());
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }
}
