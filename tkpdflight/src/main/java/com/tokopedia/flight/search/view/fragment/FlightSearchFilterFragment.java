package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.utils.CommonUtils;
import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionLabelView;
import com.tokopedia.design.label.selection.text.SelectionTextLabelView;
import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.design.text.DecimalRangeInputView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.flight.search.view.model.statistic.FlightSearchStatisticModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightSearchFilterFragment extends BaseDaggerFragment {

    public static FlightSearchFilterFragment newInstance() {

        Bundle args = new Bundle();

        FlightSearchFilterFragment fragment = new FlightSearchFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnFlightSearchFilterFragmentListener onFilterFragmentListener;
    public interface OnFlightSearchFilterFragmentListener{
        void onTransitLabelClicked();
        void onAirlineLabelClicked();
        void onRefundLabelClicked();
        void onDepartureLabelClicked();
        FlightSearchStatisticModel getFlightSearchStatisticModel();
        FlightFilterModel getFlightFilterModel();
        void onFilterModelChanged(FlightFilterModel flightFilterModel);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search_filter, container, false);
        FlightFilterModel filterModel = onFilterFragmentListener.getFlightFilterModel();
        FlightSearchStatisticModel statModel = onFilterFragmentListener.getFlightSearchStatisticModel();

        PriceRangeInputView priceRangeInputView = (PriceRangeInputView) view.findViewById(R.id.price_range_input_view);
        int statMinPrice = statModel.getMinPrice();
        int statMaxPrice = statModel.getMaxPrice();
        int filterMinPrice = filterModel.getPriceMin();
        int filterMaxPrice = filterModel.getPriceMax();
        if (filterMinPrice < statMinPrice) {
            filterMinPrice = statMinPrice;
            filterModel.setPriceMin(statMinPrice);
        }
        if (filterMaxPrice > statMaxPrice) {
            filterMaxPrice = statMaxPrice;
            filterModel.setPriceMax(filterMaxPrice);
        }
        priceRangeInputView.setPower(1);
        priceRangeInputView.setData(statMinPrice, statMaxPrice, filterMinPrice, filterMaxPrice);

        DecimalRangeInputView durationDecimalRangeInputView = (DecimalRangeInputView) view.findViewById(R.id.duration_range_input_view);
        SelectionTextLabelView transitSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_transit);
        SelectionTextLabelView airlineSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_airline);
        SelectionTextLabelView departureTimeSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_departure_time);
        SelectionTextLabelView refundPolicySelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_refund_policy);
        transitSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onTransitLabelClicked();
            }
        });
        List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SelectionItem<String> selectionItem = new SelectionItem<>();
            selectionItem.setKey(String.valueOf(i));
            selectionItem.setValue(String.valueOf(i));
            selectionItemList.add(selectionItem);
        }

        int statMinDur = statModel.getMinDuration();
        int statMaxDur = statModel.getMaxDuration();
        int filterMinDur = filterModel.getDurationMin();
        int filterMaxDur = filterModel.getDurationMax();
        if (filterMinDur < statMinDur) {
            filterMinDur = statMinDur;
            filterModel.setDurationMin(statMinDur);
        }
        if (filterMaxDur > statMaxDur) {
            filterMaxDur = statMaxDur;
            filterModel.setDurationMax(statMaxDur);
        }
        durationDecimalRangeInputView.setPower(1);
        durationDecimalRangeInputView.setData(statMinDur, statMaxDur, filterMinDur, filterMaxDur);

        transitSelectionTextLabelView.setItemList(selectionItemList);
        transitSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                CommonUtils.dumper(selectionItem.getKey() + " - " + selectionItem.getValue());
            }
        });
        airlineSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onAirlineLabelClicked();
            }
        });
        airlineSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {

            }
        });
        departureTimeSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onDepartureLabelClicked();
            }
        });
        departureTimeSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {

            }
        });
        refundPolicySelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onRefundLabelClicked();
            }
        });
        refundPolicySelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {

            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        onFilterFragmentListener = (OnFlightSearchFilterFragmentListener) context;
    }
}
