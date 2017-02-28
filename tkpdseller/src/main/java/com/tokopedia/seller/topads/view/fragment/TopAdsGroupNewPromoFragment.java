package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsEditPromoNewProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsEditPromoShopActivity;

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

    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = new Intent(getActivity(), TopAdsEditPromoNewProductActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSubmitFormChooseGroup(int choosenId) {

    }

}
