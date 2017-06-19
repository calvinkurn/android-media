package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordEditDetailPositiveActivity;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailStatisticFragment;
import com.tokopedia.seller.topads.view.model.Ad;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailFragment extends TopAdsDetailStatisticFragment<TopAdsDetailGroupPresenter> {

    private LabelView keywordLabelView;
    private LabelView promoGroupLabelView;

    protected KeywordAd keywordAd;

    @Inject
    TopadsKeywordDetailPresenter topadsKeywordDetailPresenter;

    public static Fragment createInstance(KeywordAd ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordDetailComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .topAdsKeywordDetailModule(new TopAdsKeywordDetailModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        keywordLabelView = (LabelView) view.findViewById(R.id.keyword);
        promoGroupLabelView = (LabelView) view.findViewById(R.id.label_view_promo_group);

        name.setTitle(getString(R.string.top_ads_keyword_label_type));
        promoGroupLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPromoGroupClicked();
            }
        });
        topadsKeywordDetailPresenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        topadsKeywordDetailPresenter.turnOnAd(ad.getId(), keywordAd.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        topadsKeywordDetailPresenter.turnOffAd(ad.getId(), keywordAd.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void refreshAd() {
        if (keywordAd != null) {
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), keywordAd.getId(), getKeywordTypeValue(), SessionHandler.getShopID(getActivity()));
        } else {
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), adId, getKeywordTypeValue(), SessionHandler.getShopID(getActivity()));
        }
    }

    @Override
    protected void editAd() {
        startActivityForResult(TopAdsKeywordEditDetailPositiveActivity.createInstance(getActivity(), keywordAd), REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        topadsKeywordDetailPresenter.deleteAd(ad.getId(), keywordAd.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    public void onAdLoaded(Ad ad) {
        keywordAd = (KeywordAd) ad;
        super.onAdLoaded(ad);
    }

    @Override
    protected void updateMainView(Ad ad) {
        super.updateMainView(ad);
        keywordLabelView.setContent(keywordAd.getKeywordTag());
        promoGroupLabelView.setContent(keywordAd.getGroupName());
        name.setContent(keywordAd.getkeywordTypeDesc());
    }

    protected int getKeywordTypeValue() {
        return TopAdsConstant.KEYWORD_TYPE_POSITIVE_VALUE;
    }

    @Override
    protected void updateDailyBudgetView(Ad ad) {
        // Empty daily budget
    }

    private void onPromoGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, keywordAd.getGroupId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.top_ads_keyword_title_delete), getString(R.string.top_ads_keyword_confirmation_delete));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}