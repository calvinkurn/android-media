package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailNewGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailNewProductActivity;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsGroupNewPromoFragment extends TopAdsBaseManageGroupPromoFragment {

    public static final int REQUEST_CODE_AD_STATUS = 2;

    public static TopAdsGroupNewPromoFragment createInstance(String itemIdToAdd) {
        TopAdsGroupNewPromoFragment fragment = new TopAdsGroupNewPromoFragment();
        Bundle args = new Bundle();
        args.putString(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        fragment.setupArguments(args);
        return fragment;
    }

    @Override
    protected String getTextInfoChooseGroupOption() {
        return getString(R.string.label_top_ads_info_group_option_exist);
    }

    @Override
    protected String getTextInfoNewGroupOption() {
        return "";
    }

    @Override
    protected boolean getVisibleInfoChooseGroupOption() {
        return true;
    }

    @Override
    protected boolean getVisibleInfoNewGroupOption() {
        return false;
    }

    @Override
    protected void onSubmitFormNewGroup(String groupName) {
        TopAdsDetailNewGroupActivity.startNewGroup(getActivity(),
                REQUEST_CODE_AD_STATUS,
                groupName,
                itemIdToAdd);
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    protected void onSubmitFormChooseGroup(String choosenId) {
        if (!inputChooseGroup.isEnabled()) { // has already been locked
            String groupName = inputChooseGroup.getText().toString();
            if (!TextUtils.isEmpty(groupName)) {
                TopAdsDetailNewGroupActivity.startEditExistingGroup(
                        getActivity(),
                        REQUEST_CODE_AD_STATUS,
                        choosenId,
                        groupName,
                        itemIdToAdd
                );
            }

        }

    }

    @Override
    protected String getTitleButtonNext() {
        return getString(R.string.title_next);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AD_STATUS && data != null) {
            boolean adStatusChanged = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                Intent intent = new Intent();
                intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}