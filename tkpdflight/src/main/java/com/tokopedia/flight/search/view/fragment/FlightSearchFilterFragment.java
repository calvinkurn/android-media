package com.tokopedia.flight.search.view.fragment;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightSearchFilterFragment extends BaseDaggerFragment {

    public static FlightSearchFilterFragment getInstance() {
        return new FlightSearchFilterFragment();
    }

    private PriceRangeInputView priceRangeInputView;
    private DecimalRangeInputView durationDecimalRangeInputView;
    private SelectionTextLabelView transitSelectionTextLabelView;
    private SelectionTextLabelView airplaneSelectionTextLabelView;
    private SelectionTextLabelView departureTimeSelectionTextLabelView;
    private SelectionTextLabelView refundPolicySelectionTextLabelView;

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search, container, false);
        priceRangeInputView = (PriceRangeInputView) view.findViewById(R.id.price_range_input_view);
        durationDecimalRangeInputView = (DecimalRangeInputView) view.findViewById(R.id.duration_range_input_view);
        transitSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_transit);
        airplaneSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_airplane);
        departureTimeSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_departure_time);
        refundPolicySelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_refund_policy);
        transitSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SelectionItem<String> selectionItem = new SelectionItem<>();
            selectionItem.setKey(String.valueOf(i));
            selectionItem.setValue(String.valueOf(i));
            selectionItemList.add(selectionItem);
        }

        durationDecimalRangeInputView.setData(0, 10000, 100, 5000);
        transitSelectionTextLabelView.setItemList(selectionItemList);
        transitSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                CommonUtils.dumper(selectionItem.getKey() + " - " + selectionItem.getValue());
            }
        });
        airplaneSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        airplaneSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {

            }
        });
        departureTimeSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
