package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailEditGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailEditProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailGroupPresenterImpl;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

/**
 * Created by zulfikarrahman on 1/3/17.
 */

public class TopAdsDetailGroupFragment extends TopAdsDetailFragment<TopAdsDetailGroupPresenter> {

    private TopAdsLabelView items;

    private GroupAd ad;

    public static Fragment createInstance(GroupAd groupAd, String adIs) {
        Fragment fragment = new TopAdsDetailGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, groupAd);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adIs);
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
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_group_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(ad.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(ad.getId());
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
    protected void editAd() {
        Intent intent = new Intent(getActivity(), TopAdsDetailEditGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, ad.getName());
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, String.valueOf(ad.getId()));
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        presenter.deleteAd(ad.getId());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        super.onAdLoaded(ad);
        this.ad = (GroupAd) ad;
        items.setContent(String.valueOf(this.ad.getTotalItem()));
        if(this.ad.getTotalItem() > 0){
            items.setVisibleArrow(true);
            items.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        }
    }

    void onProductItemClicked() {
        if (ad != null) {
            Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP, ad);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.title_delete_group), getString(R.string.top_ads_delete_group_alert));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}