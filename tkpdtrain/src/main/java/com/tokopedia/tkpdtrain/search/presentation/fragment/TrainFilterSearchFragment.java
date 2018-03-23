package com.tokopedia.tkpdtrain.search.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.search.domain.FilterSearchData;
import com.tokopedia.tkpdtrain.search.presentation.contract.FilterSearchActionView;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchFragment extends BaseDaggerFragment {

    private FilterSearchActionView listener;
    private FilterSearchData filterSearchData;
    private CurrencyTextWatcher minCurrencyTextWatcher;
    private CurrencyTextWatcher maxCurrencyTextWatcher;

    public static TrainFilterSearchFragment newInstance() {
        TrainFilterSearchFragment fragment = new TrainFilterSearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_filter_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSearchData = listener.getFilterSearchData();

        renderPriceRangeFilter(view);
    }

    private void renderPriceRangeFilter(View view) {
        RangeInputView rangeInputView = view.findViewById(R.id.price_filter_search);
        EditText minPriceEditText = rangeInputView.getMinValueEditText();
        EditText maxPriceEditText = rangeInputView.getMaxValueEditText();

        if (minCurrencyTextWatcher != null) {
            minPriceEditText.removeTextChangedListener(minCurrencyTextWatcher);
        }
        minCurrencyTextWatcher = new CurrencyTextWatcher(minPriceEditText, CurrencyEnum.RP);
        minPriceEditText.addTextChangedListener(minCurrencyTextWatcher);

        if (maxCurrencyTextWatcher != null) {
            maxPriceEditText.removeTextChangedListener(maxCurrencyTextWatcher);
        }
        maxCurrencyTextWatcher = new CurrencyTextWatcher(maxPriceEditText, CurrencyEnum.RP);
        maxPriceEditText.addTextChangedListener(maxCurrencyTextWatcher);

        rangeInputView.setPower(1);
        rangeInputView.setData((int) filterSearchData.getMinPrice(),
                (int) filterSearchData.getMaxPrice(),
                (int) filterSearchData.getMinPrice(),
                (int) filterSearchData.getMaxPrice());
        rangeInputView.setOnValueChangedListener(new RangeInputView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue, int minBound, int maxBound) {
                FilterSearchData filterSearchData = listener.getFilterSearchData();
                filterSearchData.setMinPrice(minValue);
                filterSearchData.setMaxPrice(maxValue);
                listener.onChangeFilterSearchData(filterSearchData);
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (FilterSearchActionView) context;
    }
}
