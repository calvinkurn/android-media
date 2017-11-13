package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailPriceFragment extends Fragment {

    private static final String EXTRA_FLIGHT_SEARCH_MODEL = "EXTRA_FLIGHT_SEARCH_MODEL";
    private FlightSearchViewModel flightSearchViewModel;

    private TextView labelAdultPrice;
    private TextView labelChildPrice;
    private TextView labelInfantPrice;
    private TextView adultPrice;
    private TextView childPrice;
    private TextView infantPrice;
    private TextView normalPrice;
    private TextView savingPrice;
    private TextView totalPrice;
    private View containerChildPrice;
    private View containerInfantPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightSearchViewModel = getArguments().getParcelable(EXTRA_FLIGHT_SEARCH_MODEL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail_price, container, false);
        labelAdultPrice = (TextView) view.findViewById(R.id.label_adult_price);
        labelChildPrice = (TextView) view.findViewById(R.id.label_child_price);
        labelInfantPrice = (TextView) view.findViewById(R.id.label_infant_price);
        adultPrice = (TextView) view.findViewById(R.id.adult_price);
        childPrice = (TextView) view.findViewById(R.id.child_price);
        infantPrice = (TextView) view.findViewById(R.id.infant_price);
        normalPrice = (TextView) view.findViewById(R.id.normal_price);
        savingPrice = (TextView) view.findViewById(R.id.saving_price);
        totalPrice = (TextView) view.findViewById(R.id.total_price);
        containerChildPrice = view.findViewById(R.id.container_child_price);
        containerInfantPrice = view.findViewById(R.id.container_infant_price);

        updateView();
        return view;
    }

    void updateView() {
        adultPrice.setText(flightSearchViewModel.getFare().getAdult());
        labelAdultPrice.setText(R.string.flight_label_adult);
        if(flightSearchViewModel.getFare().getChildNumeric() > 0){
            containerChildPrice.setVisibility(View.VISIBLE);
            labelChildPrice.setText(R.string.flight_label_child);
            childPrice.setText(flightSearchViewModel.getFare().getChild());
        }
        if(flightSearchViewModel.getFare().getInfantNumeric() > 0){
            containerInfantPrice.setVisibility(View.VISIBLE);
            labelInfantPrice.setText(R.string.flight_label_infant);
            infantPrice.setText(flightSearchViewModel.getFare().getInfant());
        }
        normalPrice.setText(flightSearchViewModel.getBeforeTotal());
        savingPrice.setText(flightSearchViewModel.getBeforeTotal());
        totalPrice.setText(flightSearchViewModel.getTotal());
    }

    public static FlightDetailPriceFragment createInstance(FlightSearchViewModel flightSearchViewModel) {
        FlightDetailPriceFragment flightDetailPriceFragment = new FlightDetailPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_SEARCH_MODEL, flightSearchViewModel);
        flightDetailPriceFragment.setArguments(bundle);
        return flightDetailPriceFragment;
    }
}
