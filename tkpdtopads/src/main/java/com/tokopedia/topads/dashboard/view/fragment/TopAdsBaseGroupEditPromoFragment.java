package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.TopAdsGroupeditPromoDI;
import com.tokopedia.topads.dashboard.view.listener.TopAdsGroupEditPromoView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGroupEditPromoPresenter;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public abstract class TopAdsBaseGroupEditPromoFragment extends TopAdsBaseManageGroupPromoFragment<TopAdsGroupEditPromoPresenter>
        implements TopAdsGroupEditPromoView {

    public static final int NEW_GROUP = 1;
    public static final int EXIST_GROUP = 2;
    public static final int NOT_IN_GROUP = 3;

    protected String adId;
    protected int choosenOption;
    protected String groupId;
    protected String groupName;

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        adId = arguments.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        choosenOption = arguments.getInt(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP);
        groupId = arguments.getString(TopAdsExtraConstant.EXTRA_GROUP_ID);
        groupName = arguments.getString(TopAdsExtraConstant.EXTRA_GROUP_NAME);
        choosenId = groupId;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        titleChoosePromo.setVisibility(View.GONE);
        switch(choosenOption){
            case EXIST_GROUP:
                viewRadioChooseGroup.setChecked(true);
                inputChooseGroup.setText(groupName);
                inputChooseGroup.lockView();
                break;
            case NOT_IN_GROUP:
                viewRadioNotInGroup.setChecked(true);
                break;
            default:
                showErrorSnackBar(getString(R.string.label_top_ads_error_choose_one_option));
        }
    }

    @Override
    public void showErrorSnackBar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void onErrorMoveOutProductGroup() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_fail_move_out_product));
    }

    @Override
    public void onSuccessMoveOutProductGroup() {
        finishAndSetResult();
    }

    @Override
    public void onSuccessMoveToNewProductGroup() {
        finishAndSetResult();
    }

    @Override
    public void onErrorMoveToNewProductGroup() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_fail_move_new_group));
    }

    @Override
    public void onSuccessMoveToExistProductGroup() {
        finishAndSetResult();
    }

    @Override
    public void onErrorMoveToExistProductGroup() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_fail_move_exist_group));
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsGroupeditPromoDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    void finishAndSetResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
