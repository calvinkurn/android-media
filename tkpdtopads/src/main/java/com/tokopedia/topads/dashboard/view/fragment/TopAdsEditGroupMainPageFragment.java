package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCreatePromoExistingGroupEditActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditCostExistingGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditGroupNameActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditScheduleExistingGroupActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailGroupPresenterImpl;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditGroupMainPageFragment extends TopAdsDetailEditMainPageFragment<GroupAd> {

    @Inject
    TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    @Inject
    TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase;
    private LabelView productAdd;
    private LabelView name;
    private LabelView keywordTotalAdd;

    public static Fragment createInstance(GroupAd ad, String adId) {
        Fragment fragment = new TopAdsEditGroupMainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailGroupPresenterImpl(getActivity(), this, new TopAdsGroupAdInteractorImpl(getActivity()), topAdsGetDetailGroupUseCase, topAdsGetSuggestionUseCase);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_group_main_page;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        productAdd = view.findViewById(R.id.product_add);
        name = view.findViewById(R.id.name);
        keywordTotalAdd = view.findViewById(R.id.total_keyword_add);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        name.setEnabled(false);
        productAdd.setEnabled(false);
        keywordTotalAdd.setEnabled(false);
    }

    @Override
    protected void onScheduleClicked() {
        Intent intent;
        if (ad != null) {
            intent = TopAdsEditScheduleExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        } else {
            intent = TopAdsEditScheduleExistingGroupActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    public void onAdLoaded(GroupAd groupAd) {
        super.onAdLoaded(groupAd);
        name.setEnabled(true);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded() && ad != null) {
                    startActivityForResult(TopAdsEditGroupNameActivity.createIntent(getActivity(), ad.getName(), String.valueOf(ad.getId())), REQUEST_CODE_AD_EDIT);
                }
            }
        });
        productAdd.setEnabled(true);
        productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded() && ad != null) {
                    startActivityForResult(TopAdsCreatePromoExistingGroupEditActivity.createIntent(getActivity(), String.valueOf(ad.getId()), null), REQUEST_CODE_AD_EDIT);
                }
            }
        });
        keywordTotalAdd.setEnabled(true);
        keywordTotalAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded() && ad != null) {
                    TopAdsKeywordNewChooseGroupActivity.start(TopAdsEditGroupMainPageFragment.this, getActivity(), REQUEST_CODE_AD_EDIT, true, ad.getName());
                }
            }
        });
    }

    @Override
    protected void onCostClicked() {
        Intent intent;
        if (ad != null) {
            intent = TopAdsEditCostExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId()), ad);
        } else {
            intent = TopAdsEditCostExistingGroupActivity.createIntent(getActivity(), adId, ad);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected GroupAd fillFromPrevious(GroupAd current, GroupAd previous) {
        if (ad != null && ad.getDatum() != null) {
            current.setDatum(ad.getDatum());
        } else if (previous != null && previous.getDatum() != null) {
            current.setDatum(previous.getDatum());
        }
        return current;
    }

    @Override
    protected void refreshAd() {
        showLoading();
        if (ad != null) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void updateMainView(GroupAd ad) {
        super.updateMainView(ad);
        name.setContent(ad.getName());
        productAdd.setTitle(getString(R.string.top_ads_label_count_product_group, ad.getTotalItem()));
        keywordTotalAdd.setTitle(getString(R.string.top_ads_label_count_keyword, ad.getKeywordTotal()));
    }
}
