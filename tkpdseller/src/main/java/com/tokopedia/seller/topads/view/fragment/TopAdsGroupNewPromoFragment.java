package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.topads.di.TopAdsAddPromoPoductDI;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewProductActivity;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.listener.TopAdsManageGroupPromoView;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenter;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsGroupNewPromoFragment extends TopAdsBaseManageGroupPromoFragment {

    public static TopAdsGroupNewPromoFragment createInstance() {
        TopAdsGroupNewPromoFragment fragment = new TopAdsGroupNewPromoFragment();
        return fragment;
    }

    @Override
    protected String getTextInfoChooseGroupOption() {
        return "";
    }

    @Override
    protected String getTextInfoNewGroupOption() {
        return "";
    }

    @Override
    protected boolean getVisibleInfoChooseGroupOption() {
        return false;
    }

    @Override
    protected boolean getVisibleInfoNewGroupOption() {
        return false;
    }

    @Override
    protected boolean getVisibleNotInGroupOption() {
        return true;
    }

    @Override
    protected boolean getVisibleChooseGroupOption() {
        return true;
    }

    @Override
    protected boolean getVisibleNewGroupOption() {
        return true;
    }

    @Override
    protected void onSubmitFormNewGroup(String groupName) {

    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewProductActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSubmitFormChooseGroup(int choosenId) {

    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_next);
    }


}
