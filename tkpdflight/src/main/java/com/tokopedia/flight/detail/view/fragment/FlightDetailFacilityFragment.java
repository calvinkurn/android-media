package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.utils.MethodChecker;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.adapter.FlightDetailFacilityAdapter;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityFragment extends BaseListFragment<Route> implements BaseListAdapter.OnBaseListV2AdapterListener<Route> {

    public static final String EXTRA_FLIGHT_DETAIL_MODEL = "EXTRA_FLIGHT_DETAIL_MODEL";

    private FlightDetailViewModel flightDetailViewModel;
    private TextView priceTotal;
    private TextView savingPrice;

    public static FlightDetailFacilityFragment createInstance(FlightDetailViewModel flightDetailViewModel) {
        FlightDetailFacilityFragment flightDetailFacilityFragment = new FlightDetailFacilityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_DETAIL_MODEL, flightDetailViewModel);
        flightDetailFacilityFragment.setArguments(bundle);
        return flightDetailFacilityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail, container, false);
        priceTotal = (TextView) view.findViewById(R.id.flight_price_total);
        priceTotal.setText(flightDetailViewModel.getTotal());
        savingPrice = (TextView) view.findViewById(R.id.saving_price);
        if(!TextUtils.isEmpty(flightDetailViewModel.getBeforeTotal())){
            savingPrice.setVisibility(View.VISIBLE);
            savingPrice.setText(MethodChecker.fromHtml(getString(R.string.flight_label_saving_price_html, flightDetailViewModel.getBeforeTotal())));
        }else{
            savingPrice.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightDetailViewModel = getArguments().getParcelable(EXTRA_FLIGHT_DETAIL_MODEL);
    }

    @Override
    protected BaseListAdapter<Route> getNewAdapter() {
        return new FlightDetailFacilityAdapter(this);
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightDetailViewModel.getRouteList(), flightDetailViewModel.getRouteList().size());
    }

    @Override
    public void onItemClicked(Route flightSearchData) {

    }


}
