package com.tokopedia.seller.topads.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupEditPromoFragment extends TopAdsBaseGroupEditPromoFragment{

    public static TopAdsGroupEditPromoFragment createInstance(String adId, int choosenOption,
                                                              String groupId, String groupName) {
        TopAdsGroupEditPromoFragment fragment = new TopAdsGroupEditPromoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putInt(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, choosenOption);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        fragment.setupArguments(bundle);
        return fragment;
    }

    @Override
    protected String getTextInfoChooseGroupOption() {
        return "";
    }

    @Override
    protected String getTextInfoNewGroupOption() {
        return getString(R.string.label_top_ads_info_group_option);
    }

    @Override
    protected boolean getVisibleInfoChooseGroupOption() {
        return false;
    }

    @Override
    protected boolean getVisibleInfoNewGroupOption() {
        return true;
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
        presenter.moveToNewProductGroup(adId, groupName, SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        presenter.moveOutProductGroup(SessionHandler.getShopID(getActivity()),adId);
    }

    @Override
    protected void onSubmitFormChooseGroup(String choosenId) {
        presenter.moveToExistProductGroup(adId, choosenId , SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_save);
    }

}
