package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.search.view.DiscoverySearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IntermediaryActivity extends BasePresenterActivity implements MenuItemCompat.OnActionExpandListener {

    private FragmentManager fragmentManager;
    MenuItem searchItem;
    public static final String CATEGORY_DEFAULT_TITLE = "Direktori";

    private String departmentId = "";
    private String categoryName = CATEGORY_DEFAULT_TITLE;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.progressBar)
    ProgressBar progressBar;

    @BindView(R2.id.container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void inflateView(int layoutId) {
        setContentView(layoutId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CategoryNavigationActivity.DESTROY_INTERMEDIARY) {
            finish();
        }
    }

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

    public static void moveTo(Context context, String depId) {
        if (context == null)
            return;

        Intent intent = new Intent(context, IntermediaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        departmentId = extras.getString(BrowseProductRouter.DEPARTMENT_ID);
        if (extras.getString(BrowseProductRouter.DEPARTMENT_NAME)!=null
                && extras.getString(BrowseProductRouter.DEPARTMENT_NAME).length()>0)
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
        ButterKnife.bind(this);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void setViewListener() {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchOptionSelected();
            }
        });
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
            fragment = IntermediaryFragment.createInstance(departmentId);
        } else if (fragment instanceof IntermediaryFragment) {
            ((IntermediaryFragment)fragment).setDepartmentId(departmentId);
        }
        inflateFragment(
                fragment,
                false,
                IntermediaryFragment.TAG);
    }

    protected boolean onSearchOptionSelected() {
        Intent intent = BrowseProductRouter
                .getBrowseProductIntent(this, departmentId, TopAdsApi.SRC_DIRECTORY);
        startActivity(intent);
        return true;
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        if (isFinishing()) return;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if (fragment instanceof BrowseParentFragment) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            CommonUtils.hideKeyboard(this, getCurrentFocus());
        } else {
            params.setScrollFlags(0);
        }
        toolbar.setLayoutParams(params);
        toolbar.requestLayout();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        return true;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }
}
