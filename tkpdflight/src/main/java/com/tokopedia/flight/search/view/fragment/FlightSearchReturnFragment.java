package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment {

    public static FlightSearchReturnFragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        FlightSearchReturnFragment fragment = new FlightSearchReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_return, container, false);
    }

    protected boolean isReturning(){
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

}
