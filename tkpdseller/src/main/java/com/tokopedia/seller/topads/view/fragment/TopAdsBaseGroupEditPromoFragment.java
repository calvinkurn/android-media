package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsGroupeditPromoDI;
import com.tokopedia.seller.topads.view.listener.TopAdsGroupEditPromoView;
import com.tokopedia.seller.topads.view.presenter.TopAdsGroupEditPromoPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenter;

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
        adId = arguments.getString(TopAdsExtraConstant.AD_ID);
        choosenOption = arguments.getInt(TopAdsExtraConstant.CHOOSEN_OPTION);
        groupId = arguments.getString(TopAdsExtraConstant.GROUP_ID);
        groupName = arguments.getString(TopAdsExtraConstant.GROUP_NAME);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        switch(choosenOption){
            case NEW_GROUP:
                viewRadioNewGroup.setChecked(true);
                inputNewGroup.setText(groupName);
                break;
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
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onSuccessMoveToNewProductGroup() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorMoveToNewProductGroup() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_fail_move_new_group));
    }

    @Override
    public void onSuccessMoveToExistProductGroup() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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
}
