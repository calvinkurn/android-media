package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailPriceFragment extends Fragment {

    private static final String EXTRA_FLIGHT_DETAIL_MODEL = "EXTRA_FLIGHT_DETAIL_MODEL";
    private FlightDetailViewModel flightDetailViewModel;

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
    private View containerSavingPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightDetailViewModel = getArguments().getParcelable(EXTRA_FLIGHT_DETAIL_MODEL);
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
        containerSavingPrice = view.findViewById(R.id.container_saving_price);

        updateView();
        return view;
    }

    void updateView() {
        double adultPriceTotal = flightDetailViewModel.getAdultNumericPrice() * flightDetailViewModel.getCountAdult();
        adultPrice.setText(getString(R.string.flight_label_currency, CurrencyFormatUtil.getThousandSeparatorString(adultPriceTotal, false, 0).getFormattedString()));
        labelAdultPrice.setText(getString(R.string.flight_label_adult, flightDetailViewModel.getCountAdult()));
        if (flightDetailViewModel.getCountChild() > 0) {
            containerChildPrice.setVisibility(View.VISIBLE);
            labelChildPrice.setText(getString(R.string.flight_label_child, flightDetailViewModel.getCountChild()));
            double childPriceTotal = flightDetailViewModel.getChildNumericPrice() * flightDetailViewModel.getCountChild();
            childPrice.setText(getString(R.string.flight_label_currency, CurrencyFormatUtil.getThousandSeparatorString(childPriceTotal, false, 0).getFormattedString()));
        }
        if (flightDetailViewModel.getCountInfant() > 0) {
            containerInfantPrice.setVisibility(View.VISIBLE);
            labelInfantPrice.setText(getString(R.string.flight_label_infant, flightDetailViewModel.getCountInfant()));
            double infantPriceTotal = flightDetailViewModel.getInfantNumericPrice() * flightDetailViewModel.getCountInfant();
            infantPrice.setText(getString(R.string.flight_label_currency, CurrencyFormatUtil.getThousandSeparatorString(infantPriceTotal, false, 0).getFormattedString()));
        }
        if (!TextUtils.isEmpty(flightDetailViewModel.getBeforeTotal())) {
            containerSavingPrice.setVisibility(View.VISIBLE);
            normalPrice.setText(flightDetailViewModel.getBeforeTotal());
            savingPrice.setText(flightDetailViewModel.getBeforeTotal());
        } else {
            containerSavingPrice.setVisibility(View.GONE);
        }
        totalPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(calculateTotal(flightDetailViewModel)));
    }

    private int calculateTotal(FlightDetailViewModel flightDetailViewModel) {
        int total = flightDetailViewModel.getCountAdult() * flightDetailViewModel.getAdultNumericPrice();
        total += flightDetailViewModel.getCountChild() * flightDetailViewModel.getChildNumericPrice();
        total += flightDetailViewModel.getCountInfant() * flightDetailViewModel.getInfantNumericPrice();
        return total;
    }

    public static FlightDetailPriceFragment createInstance(FlightDetailViewModel flightDetailViewModel) {
        FlightDetailPriceFragment flightDetailPriceFragment = new FlightDetailPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_DETAIL_MODEL, flightDetailViewModel);
        flightDetailPriceFragment.setArguments(bundle);
        return flightDetailPriceFragment;
    }
}
