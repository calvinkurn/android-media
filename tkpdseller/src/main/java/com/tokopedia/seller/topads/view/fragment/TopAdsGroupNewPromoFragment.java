package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewProductActivity;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsGroupNewPromoFragment extends TopAdsBaseManageGroupPromoFragment {

    protected static final int REQUEST_CODE_AD_STATUS = 2;

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
    protected boolean getVIsibleInfoNewGroupOption() {
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
    protected void onSubmitFormNewGroup(String GroupName) {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewGroupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewProductActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    protected void onSubmitFormChooseGroup(int choosenId) {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewGroupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

}