package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.topads.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCreatePromoExistingGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCreatePromoExistingGroupEditActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditCostExistingGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditGroupNameActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditScheduleExistingGroupActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditGroupMainPageFragment extends TopAdsDetailEditMainPageFragment<GroupAd> {

    private LabelView productAdd;
    private LabelView name;

    public static Fragment createInstance(GroupAd ad, String adId) {
        Fragment fragment = new TopAdsEditGroupMainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailGroupPresenterImpl(getActivity(), this, new TopAdsGroupAdInteractorImpl(getActivity()));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_group_main_page;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        productAdd = (LabelView) view.findViewById(R.id.product_add);
        name = (LabelView) view.findViewById(R.id.name);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TopAdsEditGroupNameActivity.createIntent(getActivity(), ad.getName(), String.valueOf(ad.getId())), REQUEST_CODE_AD_EDIT);
            }
        });
        productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TopAdsCreatePromoExistingGroupEditActivity.createIntent(getActivity(), String.valueOf(ad.getId()), null), REQUEST_CODE_AD_EDIT);
            }
        });
    }

    @Override
    protected void onScheduleClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditScheduleExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditScheduleExistingGroupActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void onCostClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditCostExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditCostExistingGroupActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void refreshAd() {
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
    }
}
