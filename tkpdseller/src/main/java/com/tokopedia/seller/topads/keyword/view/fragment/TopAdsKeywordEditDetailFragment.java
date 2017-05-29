package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenter;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordEditDetailView;
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

    public static Bundle createArguments(
            TopAdsKeywordEditDetailViewModel model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEYWORD_DETAIL_MODEL, model);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        TopAdsKeywordEditDetailViewModel model = bundle.getParcelable(KEYWORD_DETAIL_MODEL);

        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);

        SpinnerTextView spinnerTextView = ((SpinnerTextView) view.findViewById(R.id.spinner_text_view_top_ads_keyword_type));
        spinnerTextView.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_names));
        spinnerTextView.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_list_values));

        ((EditText)view.findViewById(R.id.edit_text_top_ads_keyword)).setText(model.getKeywordText());

        EditText costPerClick = (EditText) view.findViewById(R.id.edit_text_top_ads_cost_per_click);
        CurrencyIdrTextWatcher textWatcher = new CurrencyIdrTextWatcher(costPerClick);
        costPerClick.addTextChangedListener(textWatcher);
        costPerClick.setText(String.valueOf(model.getCostPerClick()));

        view.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
