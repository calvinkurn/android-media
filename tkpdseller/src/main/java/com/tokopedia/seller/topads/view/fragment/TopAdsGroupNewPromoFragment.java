package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewProductActivity;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsGroupNewPromoFragment extends TopAdsBaseManageGroupPromoFragment {

    public static final int REQUEST_CODE_AD_STATUS = 2;

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
        TopAdsDetailNewGroupActivity.startNewGroup(getActivity(),
                REQUEST_CODE_AD_STATUS,
                groupName);
    }

    @Override
    protected void onSubmitFormNotInGroup() {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewProductActivity.class);
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
                        groupName
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