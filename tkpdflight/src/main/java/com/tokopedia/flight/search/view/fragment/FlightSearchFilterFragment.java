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

    private SelectionTextLabelView transitSelectionTextLabelView;

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search, container, false);
        PriceRangeInputView priceRangeInputView = (PriceRangeInputView) view.findViewById(R.id.price_range_input_view);
        transitSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_transit);
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

        transitSelectionTextLabelView.setItemList(selectionItemList);
        transitSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                CommonUtils.dumper(selectionItem.getKey() + " - " + selectionItem.getValue());
            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
