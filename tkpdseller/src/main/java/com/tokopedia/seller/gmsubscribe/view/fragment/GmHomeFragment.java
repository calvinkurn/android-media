package com.tokopedia.seller.gmsubscribe.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.gmsubscribe.di.GmHomeDependencyInjection;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmHomePresenterImpl;

import butterknife.OnClick;

/**
 * Created by sebastianuskh on 12/2/16.
 */

public class GmHomeFragment extends BasePresenterFragment<GmHomePresenterImpl> implements GmHomeView {

    public static final String TAG = "GMHomeFragment";
    private GmHomeFragmentCallback listener;
    private TkpdProgressDialog progressDialog;

    /**
     * CONSTRUCTOR AREA
     */
    public static Fragment createFragment() {
        return new GmHomeFragment();
    }

    /**
     * BUTTERKNIFE BINDING
     */
    @OnClick(R2.id.button_home_to_select_product)
    public void goToProductSelection() {
        listener.goToGMProductFristTime();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    public void onResume() {
        super.onResume();
        listener.changeActionBarTitle(getString(R.string.gm_home_title));
        listener.setDrawer(true);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = GmHomeDependencyInjection.getPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof GmHomeFragmentCallback) {
            listener = (GmHomeFragmentCallback) activity;
        } else {
            throw new RuntimeException("Please implement GMHomeFragmentListener in the Activity");
        }
    }

    @Override
    protected void setupArguments(Bundle bundle) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gmsubscribe_home;
    }

    @Override
    protected void initView(View view) {
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {
        presenter.clearGMProductCache();
    }


    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
