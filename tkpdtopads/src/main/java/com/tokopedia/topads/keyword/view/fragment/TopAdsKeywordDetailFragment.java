package com.tokopedia.topads.keyword.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailStatisticFragment;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordDetailComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordEditDetailPositiveActivity;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailFragment extends TopAdsDetailStatisticFragment<TopAdsDetailGroupPresenter, KeywordAd> {

    private LabelView keywordLabelView;
    private LabelView promoGroupLabelView;

    OnKeywordDetailListener onKeywordDetailListener;
    public interface OnKeywordDetailListener{
        void startShowCase();
    }
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
                .topAdsComponent(getTopAdsComponent())
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
        topadsKeywordDetailPresenter.turnOnAd(ad.getId(), ad.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        topadsKeywordDetailPresenter.turnOffAd(ad.getId(), ad.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void refreshAd() {
        if (adFromIntent != null) {
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), adFromIntent.getId(), getKeywordTypeValue(), SessionHandler.getShopID(getActivity()));
        } else {
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), adId, getKeywordTypeValue(), SessionHandler.getShopID(getActivity()));
        }
    }

    @Override
    public void onAdLoaded(KeywordAd ad) {
        super.onAdLoaded(ad);
        if (onKeywordDetailListener!= null) {
            onKeywordDetailListener.startShowCase();
        }
    }

    @Override
    protected void editAd() {
        startActivityForResult(TopAdsKeywordEditDetailPositiveActivity.createInstance(getActivity(), ad), REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        topadsKeywordDetailPresenter.deleteAd(ad.getId(), ad.getGroupId(), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void updateMainView(KeywordAd ad) {
        super.updateMainView(ad);
        keywordLabelView.setContent(ad.getKeywordTag());
        promoGroupLabelView.setContent(ad.getGroupName());
        name.setContent(ad.getkeywordTypeDesc());
    }

    protected int getKeywordTypeValue() {
        return TopAdsConstant.KEYWORD_TYPE_POSITIVE_VALUE;
    }

    @Override
    protected void updateDailyBudgetView(KeywordAd ad) {
        // Empty daily budget
    }

    private void onPromoGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, ad.getGroupId());
        intent.putExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, true);
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
        topadsKeywordDetailPresenter.unSubscribe();
    }

    // for show case
    public View getStatusView(){
        return getView().findViewById(R.id.status);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof OnKeywordDetailListener) {
            onKeywordDetailListener = (OnKeywordDetailListener) context;
        }
    }
}