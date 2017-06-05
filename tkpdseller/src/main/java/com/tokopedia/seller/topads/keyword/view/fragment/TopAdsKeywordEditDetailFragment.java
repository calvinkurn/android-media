package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordEditDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordEditDetailModule;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordEditDetailView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 5/23/17.
 */

public abstract class TopAdsKeywordEditDetailFragment extends BaseDaggerFragment implements TopAdsKeywordEditDetailView {
    public static final String TAG = "TopAdsKeywordEditDetailFragment";

    @Inject
    TopAdsKeywordEditDetailPresenter presenter;
    protected SpinnerTextView topAdsKeywordType;
    protected EditText topAdsKeyword;
    protected EditText topAdsCostPerClick;
    protected TextView topAdsMaxPriceInstruction;

    private KeywordAd keywordAd;

    public static Bundle createArguments(KeywordAd model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, model);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        keywordAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);

        settingTopAdsKeywordType(view);
        settingTopAdsKeyword(view);
        settingTopAdsCostPerClick(view);

        if (keywordAd != null) {
            fillDataToView(keywordAd);
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

    private void fillDataToView(KeywordAd keywordAd) {
        topAdsKeywordType.setSpinnerValue(keywordAd.getKeywordTypeId());
        topAdsKeyword.setText(keywordAd.getName());
        topAdsCostPerClick.setText(CurrencyFormatHelper.removeCurrencyPrefix(keywordAd.getPriceBidFmt()));
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

    private KeywordAd collectDataFromView() {
        if (!TextUtils.isEmpty(topAdsKeywordType.getSpinnerValue())) {
            keywordAd.setKeywordTypeId(topAdsKeywordType.getSpinnerValue());
        }
        keywordAd.setKeywordTag(getTopAdsKeywordText());
        keywordAd.setPriceBidFmt(getTopAdsCostPerClick());
        return keywordAd;
    }

    public String getTopAdsKeywordText() {
        return topAdsKeyword.getText().toString();
    }

    public String getTopAdsCostPerClick() {
        return topAdsCostPerClick.getText().toString();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessEditTopAdsKeywordDetail(KeywordAd viewModel) {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showError(String detail) {
        NetworkErrorHelper.showSnackbar(getActivity(), detail);
    }
}