package com.tokopedia.seller.topads.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsAddPromoPoductDI;
import com.tokopedia.seller.topads.view.listener.TopAdsManageGroupPromoView;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenter;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupManagePromoFragment extends TopAdsBaseGroupEditPromoFragment {

    public static TopAdsGroupManagePromoFragment createInstance(String adId, int choosenOption,
                                                              String groupId, String groupName) {
        TopAdsGroupManagePromoFragment fragment = new TopAdsGroupManagePromoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.AD_ID, adId);
        bundle.putInt(TopAdsExtraConstant.CHOOSEN_OPTION, choosenOption);
        bundle.putString(TopAdsExtraConstant.GROUP_ID, groupId);
        bundle.putString(TopAdsExtraConstant.GROUP_NAME, groupName);
        fragment.setupArguments(bundle);
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
        return false;
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
        presenter.moveOutProductGroup(SessionHandler.getShopID(getActivity()), adId);
    }

    @Override
    protected void onSubmitFormChooseGroup(int choosenId) {
        presenter.moveToExistProductGroup(adId, String.valueOf(choosenId), SessionHandler.getShopID(getActivity()));
    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_save);
    }
}
