package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditGroupNameFragment extends BasePresenterFragment {

    @Inject
    TopAdsDetailEditGroupPresenter topAdsDetailEditGroupPresenter;

    protected TextInputLayout nameInputLayout;
    protected EditText nameEditText;
    protected Button buttonNext;

    public static Fragment createInstance(String name, String adId) {
        Fragment fragment = new TopAdsDetailEditGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        buttonNext = (Button) view.findViewById(R.id.button_submit);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                topAdsDetailEditGroupPresenter.saveAd();
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_group_name;
    }
}
