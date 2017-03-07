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
                                                              int groupId, String groupName) {
        TopAdsGroupEditPromoFragment fragment = new TopAdsGroupEditPromoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putInt(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, choosenOption);
        bundle.putInt(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        fragment.setupArguments(bundle);
        return fragment;
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
    protected void onSubmitFormChooseGroup(int choosenId) {
        presenter.moveToExistProductGroup(adId, String.valueOf(choosenId) , SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_save);
    }

}
