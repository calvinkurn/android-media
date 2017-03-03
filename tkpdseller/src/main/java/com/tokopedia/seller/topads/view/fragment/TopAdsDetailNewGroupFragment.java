package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewGroupDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Activity to Create New Ads in Top Ads Group
 * Pass GroupId if the group has already existed, dont pass (or pass 0) if not exist
 * Pass group name to show the group to be created/edited
 * Created by Hendry on 02.03.2017
 */

public class TopAdsDetailNewGroupFragment extends TopAdsDetailNewFragment<TopAdsDetailNewGroupPresenter>
    implements TopAdsDetailNewGroupView {

    private EditText mGroupNameEditText;
    private String mGroupName;
    private int mGroupId;

    public static Fragment createInstance(String groupName, int groupId) {
        Fragment fragment = new TopAdsDetailNewGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        bundle.putInt(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        mGroupName = bundle.getString(TopAdsExtraConstant.EXTRA_GROUP_NAME);
        mGroupId = bundle.getInt(TopAdsExtraConstant.EXTRA_GROUP_ID);
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewGroupDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_new_group;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameEditText.setEnabled(false);
        nameInputLayout.setHint(getString(R.string.label_top_ads_group_name));
        setHintToGroup(view);
        checkToHideViews(view);

        initDetailAd();
    }

    /**
     * change hint of the view to "Nama group".
     */
    private void setHintToGroup (View view){
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        mGroupNameEditText = (EditText) textInputLayout.findViewById(R.id.edit_text_name);
        mGroupNameEditText.setHint(R.string.label_top_ads_group_name);
        textInputLayout.setHint(getString( R.string.label_top_ads_group_name) );
        mGroupNameEditText.setText(mGroupName);
    }

    /**
     * detail Ad (object at the parent) needs to initialize to store the selection
     */
    private void initDetailAd(){
        if (detailAd == null) {
            detailAd = new TopAdsDetailGroupViewModel();
        }
    }

    /**
     * if the group Id member is not 0, means we want to edit the product in the existing group,
     * hide the selection
     * @param view root view to find the object to show/hide
     */
    private void checkToHideViews(View view){
        if (mGroupId == 0) {

        }
        else {
//            view.findViewById(R.id.linear_partial_top_ads_edit_ad).setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadAdDetail() {
        presenter.getDetailAd(String.valueOf(mGroupId));
    }

    @Override
    protected void saveAd() {
        if (mGroupId == 0) { // create group
            super.populateDataFromFields();
            presenter.saveAdNew(mGroupName, (TopAdsDetailGroupViewModel)detailAd, topAdsProductList);
        }
        else {
            presenter.saveAdExisting(mGroupId, topAdsProductList);
        }

    }

    @Override
    protected void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, topAdsProductList);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }

    @Override
    public void showErrorGroupEmpty() {
        // TODO show error when group is empty
    }

    @Override
    public void showLoading(boolean isShown) {
        if (isShown) {
            super.showLoading();
        }
        else {
            super.hideLoading();
        }
    }
}