package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDetailGroupPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zulfikarrahman on 1/3/17.
 */

public class TopAdsDetailGroupFragment extends TopAdsDetailFragment<TopAdsDetailGroupPresenter> {

    @BindView(R2.id.items)
    TopAdsLabelView items;

    private GroupAd groupAd;

    public static Fragment createInstance(GroupAd groupAd) {
        Fragment fragment = new TopAdsDetailGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA, groupAd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsDetailGroupPresenterImpl(getActivity(), this, new TopAdsGroupAdInteractorImpl(getActivity()));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        groupAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_group_detail;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if(checked){
            presenter.turnOnAds(groupAd, SessionHandler.getShopID(getActivity()));
        }else{
            presenter.turnOffAds(groupAd, SessionHandler.getShopID(getActivity()));
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if(groupAd != null) {
            loadAdDetail(groupAd);
            items.setContent(String.valueOf(groupAd.getTotalItem()));
        }
    }

    @OnClick(R2.id.items)
    void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP, groupAd.getId());
        startActivity(intent);
    }
}
