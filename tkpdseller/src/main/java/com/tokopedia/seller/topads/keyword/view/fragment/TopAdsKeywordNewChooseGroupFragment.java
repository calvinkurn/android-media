package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewProductActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsBaseManageGroupPromoFragment;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsKeywordNewChooseGroupFragment extends BaseDaggerFragment {

    public static final int REQUEST_CODE_AD_STATUS = 2;

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsKeywordNewChooseGroupFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_new_choose_group, container, false);
        return view;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}