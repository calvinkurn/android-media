package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsCreatePromoExistingGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsCreatePromoNewGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsCreatePromoWithoutGroupActivity;

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
    protected void onSubmitFormNewGroup(String groupName) {
        Intent intent = TopAdsCreatePromoNewGroupActivity.createIntent(getActivity(),groupName, itemIdToAdd);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = TopAdsCreatePromoWithoutGroupActivity.createIntent(getActivity(), itemIdToAdd);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    protected void onSubmitFormChooseGroup(String choosenId) {
        if (!inputChooseGroup.isEnabled()) { // has already been locked
            String groupName = inputChooseGroup.getText().toString();
            if (!TextUtils.isEmpty(groupName)) {
                startActivityForResult(TopAdsCreatePromoExistingGroupActivity.createIntent(getActivity(),
                        choosenId, itemIdToAdd), REQUEST_CODE_AD_STATUS);
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