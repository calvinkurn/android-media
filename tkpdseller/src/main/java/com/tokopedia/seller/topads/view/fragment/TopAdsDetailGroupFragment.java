package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.seller.topads.domain.model.data.Ad;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailGroupPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

/**
 * Created by zulfikarrahman on 1/3/17.
 */

public class TopAdsDetailGroupFragment extends TopAdsDetailFragment<TopAdsDetailGroupPresenter> {

    TopAdsLabelView items;

    private GroupAd groupAd;
    private int groupId;

    public static Fragment createInstance(GroupAd groupAd, int groupId) {
        Fragment fragment = new TopAdsDetailGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, groupAd);
        bundle.putInt(TopAdsExtraConstant.EXTRA_AD_ID_GROUP, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        items = (TopAdsLabelView) view.findViewById(R.id.items);
        name.setTitle(getString(R.string.label_top_ads_groups));
        name.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductItemClicked();
            }
        });
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailGroupPresenterImpl(getActivity(), this, new TopAdsGroupAdInteractorImpl(getActivity()));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        groupAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        groupId = bundle.getInt(TopAdsExtraConstant.EXTRA_AD_ID_GROUP);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_group_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(groupAd, SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(groupAd, SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void refreshAd() {
        if (groupAd != null) {
            presenter.refreshAd(startDate, endDate, groupAd.getId());
        } else {
            presenter.refreshAd(startDate, endDate, groupId);
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        super.onAdLoaded(ad);
        groupAd = (GroupAd) ad;
        items.setContent(String.valueOf(groupAd.getTotalItem()));
        if(groupAd.getTotalItem() > 0){
            items.setVisibleArrow(true);
            items.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        }
    }

    void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP, groupAd);
        startActivity(intent);
    }
}