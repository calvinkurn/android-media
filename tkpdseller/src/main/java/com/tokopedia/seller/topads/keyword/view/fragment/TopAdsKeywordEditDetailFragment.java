package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordEditDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordEditDetailModule;
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
    protected SpinnerTextView topAdsKeywordType;
    protected EditText topAdsKeyword;
    protected EditText topAdsCostPerClick;
    protected TextView topAdsMaxPriceInstruction;
    private TopAdsKeywordEditDetailViewModel model;
    private TopAdsKeywordEditDetailFragmentListener listener;

    public static Bundle createArguments(
            TopAdsKeywordEditDetailViewModel model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEYWORD_DETAIL_MODEL, model);
        return bundle;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordEditDetailComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .topAdsKeywordEditDetailModule(new TopAdsKeywordEditDetailModule())
                .build()
                .inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopAdsKeywordEditDetailFragmentListener){
            this.listener = (TopAdsKeywordEditDetailFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);

        settingTopAdsKeywordType(view);
        settingTopAdsKeyword(view);
        settingTopAdsCostPerClick(view);

        Bundle bundle = getArguments();
        model = bundle.getParcelable(KEYWORD_DETAIL_MODEL);
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
        topAdsKeywordType.setSpinnerValue(String.valueOf(model.getKeywordTypeId()));
        topAdsKeyword.setText(model.getKeywordTag());
        topAdsCostPerClick.setText(String.valueOf(model.getPriceBid()));
    }

    protected void settingTopAdsCostPerClick(View view) {
        topAdsCostPerClick = (EditText) view.findViewById(R.id.edit_text_top_ads_cost_per_click);
        topAdsMaxPriceInstruction = (TextView) view.findViewById(R.id.text_view_top_ads_max_price_description);
        CurrencyIdrTextWatcher textWatcher = new CurrencyIdrTextWatcher(topAdsCostPerClick);
        topAdsCostPerClick.addTextChangedListener(textWatcher);
    }

    private void settingTopAdsKeyword(View view) {
        topAdsKeyword = ((EditText) view.findViewById(R.id.edit_text_top_ads_keyword));
    }

    protected void settingTopAdsKeywordType(View view) {
        topAdsKeywordType = ((SpinnerTextView) view.findViewById(R.id.spinner_text_view_top_ads_keyword_type));
    }

    private TopAdsKeywordEditDetailViewModel collectDataFromView() {
        model.setKeywordTypeId(getTopAdsKeywordTypeId());
        model.setKeywordTag(getTopAdsKeywordText());
        model.setPriceBid(getTopAdsCostPerClick());
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
    public void onSuccessEditTopAdsKeywordDetail(TopAdsKeywordEditDetailViewModel viewModel) {
        listener.onSuccessEditTopAdsKeywordDetail(viewModel);
    }

    @Override
    public void showError(String detail) {
        NetworkErrorHelper.showSnackbar(getActivity(), detail);
    }

    public int getTopAdsKeywordTypeId() {
        return Integer.parseInt(topAdsKeywordType.getSpinnerValue());
    }
}
