package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordEditDetailView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 5/23/17.
 */

public abstract class TopAdsKeywordEditDetailFragment extends BaseDaggerFragment implements TopAdsKeywordEditDetailView {
    public static final String TAG = "TopAdsKeywordEditDetailFragment";
    public static final String KEYWORD_DETAIL_MODEL = "KEYWORD_DETAIL_MODEL";

    @Inject
    TopAdsKeywordEditDetailPresenter presenter;
    private SpinnerTextView topAdsKeywordType;
    private EditText topAdsKeyword;
    private EditText topAdsCostPerClick;

    public static Bundle createArguments(
            TopAdsKeywordEditDetailViewModel model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEYWORD_DETAIL_MODEL, model);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);

        settingTopAdsKeywordType(view);
        settingTopAdsKeyword(view);
        settingTopAdsCostPerClick(view);

        Bundle bundle = getArguments();
        TopAdsKeywordEditDetailViewModel model = bundle.getParcelable(KEYWORD_DETAIL_MODEL);
        if (model != null) {
            fillDataToView(model);
        }

        view.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editTopAdsKeywordDetail(collectDataFromView());
            }
        });

        presenter.attachView(this);

        return view;
    }

    private void fillDataToView(TopAdsKeywordEditDetailViewModel model) {
        topAdsKeyword.setText(model.getKeywordText());
        topAdsCostPerClick.setText(String.valueOf(model.getCostPerClick()));
    }

    private void settingTopAdsCostPerClick(View view) {
        topAdsCostPerClick = (EditText) view.findViewById(R.id.edit_text_top_ads_cost_per_click);
        CurrencyIdrTextWatcher textWatcher = new CurrencyIdrTextWatcher(topAdsCostPerClick);
        topAdsCostPerClick.addTextChangedListener(textWatcher);
    }

    private void settingTopAdsKeyword(View view) {
        topAdsKeyword = ((EditText) view.findViewById(R.id.edit_text_top_ads_keyword));
    }

    private void settingTopAdsKeywordType(View view) {
        topAdsKeywordType = ((SpinnerTextView) view.findViewById(R.id.spinner_text_view_top_ads_keyword_type));
        topAdsKeywordType.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_names));
        topAdsKeywordType.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_list_values));
    }

    private TopAdsKeywordEditDetailViewModel collectDataFromView() {
        TopAdsKeywordEditDetailViewModel model = new TopAdsKeywordEditDetailViewModel();
        model.setKeywordText(getTopAdsKeywordText());
        model.setCostPerClick(getTopAdsCostPerClick());
        return model;
    }

    public String getTopAdsKeywordText() {
        return topAdsKeyword.getText().toString();
    }

    public double getTopAdsCostPerClick() {
        String valueString = CurrencyFormatHelper
                .removeCurrencyPrefix(topAdsCostPerClick.getText().toString());
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if (TextUtils.isEmpty(valueString)) {
            return 0;
        }
        return Double.parseDouble(valueString);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
