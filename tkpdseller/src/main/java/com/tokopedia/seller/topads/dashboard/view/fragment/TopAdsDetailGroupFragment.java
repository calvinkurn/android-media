package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailEditGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 1/3/17.
 */

public class TopAdsDetailGroupFragment extends TopAdsDetailStatisticFragment<TopAdsDetailGroupPresenter> {

    public interface OnTopAdsDetailGroupListener {
        void startShowCase();
    }

    public static final String GROUP_AD_PARCELABLE = "GROUP_AD_PARCELABLE";
    private LabelView items;

    private GroupAd groupAd;

    private OnTopAdsDetailGroupListener listener;

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
        items = (LabelView) view.findViewById(R.id.items);
        name.setTitle(getString(R.string.label_top_ads_groups));
        name.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.font_black_secondary_54));
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
        presenter.turnOnAds(groupAd.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(groupAd.getId());
    }

    @Override
    protected void refreshAd() {
        if (groupAd != null) {
            presenter.refreshAd(startDate, endDate, groupAd.getId());
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
        presenter.deleteAd(groupAd.getId());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        groupAd = (GroupAd) ad;
        super.onAdLoaded(ad);
        if (listener != null) {
            listener.startShowCase();
        }
    }

    @Override
    protected void updateMainView(Ad ad) {
        super.updateMainView(ad);
        items.setContent(String.valueOf(groupAd.getTotalItem()));
        if (groupAd.getTotalItem() > 0) {
            items.setVisibleArrow(true);
            items.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        }
    }

    @Override
    public void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof OnTopAdsDetailGroupListener) {
            listener = (OnTopAdsDetailGroupListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    void onProductItemClicked() {
        if (groupAd != null) {
            Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP, groupAd);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GROUP_AD_PARCELABLE, groupAd);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        groupAd = savedInstanceState.getParcelable(GROUP_AD_PARCELABLE);
        onAdLoaded(groupAd);
    }

    // for show case
    public View getStatusView() {
        return getView().findViewById(R.id.status);
    }

    public View getProductView() {
        return items;
    }
}