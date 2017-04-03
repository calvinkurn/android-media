package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.view.adapter.IntermediaryCategoryAdapter;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.AD_SRC;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_SOURCE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.FRAGMENT_ID;

public class IntermediaryActivity extends BasePresenterActivity implements IntermediaryCategoryAdapter.CategoryListener{

    private FragmentManager fragmentManager;
    private String departmentId = "";
    private String categoryName = "";


    public static void moveTo(Context context, String depId, String categoryName) {
        if (context == null)
            return;

        Intent intent = new Intent(context, IntermediaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        bundle.putString(BrowseProductRouter.DEPARTMENT_NAME, categoryName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        departmentId = extras.getString(BrowseProductRouter.DEPARTMENT_ID);
        categoryName = extras.getString(BrowseProductRouter.DEPARTMENT_NAME);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_intermediary;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setActionVar() {
        Fragment fragment =
                (fragmentManager.findFragmentByTag(IntermediaryFragment.TAG));
        if (fragment == null) {
            fragment = IntermediaryFragment.createInstance(this);
        }
        inflateFragment(
                fragment,
                false,
                IntermediaryFragment.TAG);
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void onCategoryRevampClick(ChildCategoryModel child) {
        BrowseProductActivity.moveTo(
                IntermediaryActivity.this,
                child.getCategoryId(),
                TopAdsApi.SRC_DIRECTORY,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
                child.getCategoryName()
        );
    }
}
