package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsEditPromoProductFragment extends TopAdsBaseManagePromoProductFragment {
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

    }

    @Override
    protected void onSubmitFormChooseGroup(int choosenId) {

    }

    public static TopAdsEditPromoProductFragment createInstance() {
        TopAdsEditPromoProductFragment fragment = new TopAdsEditPromoProductFragment();
        return fragment;
    }
}
