package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.search.view.DiscoverySearchView;

public class IntermediaryActivity extends BasePresenterActivity implements MenuItemCompat.OnActionExpandListener,YoutubeViewHolder.YouTubeThumbnailLoadInProcess{

    private FragmentManager fragmentManager;
    MenuItem searchItem;
    public static final String CATEGORY_DEFAULT_TITLE = "";

    private String departmentId = "";
    private String categoryName = CATEGORY_DEFAULT_TITLE;

    Toolbar toolbar;
    DiscoverySearchView searchView;
    ProgressBar progressBar;
    FrameLayout frameLayout;

    @DeepLink(Constants.Applinks.DISCOVERY_CATEGORY_DETAIL)
    public static Intent getCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, IntermediaryActivity.class);
        return intent
                .putExtras(bundle);
    }

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

    public static void moveToClear(Context context, String depId) {
        if (context == null)
            return;

        Intent intent = new Intent(context, IntermediaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        searchView = (DiscoverySearchView) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        }else {
            toolbar.setBackgroundResource(com.tokopedia.core.R.drawable.bg_white_toolbar_drop_shadow);
        }
        Drawable drawable = ContextCompat.getDrawable(this, com.tokopedia.core.R.drawable.ic_toolbar_overflow_level_two_black);
        drawable.setBounds(5, 5, 5, 5);
        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core.R.drawable.ic_webview_back_button
            );
        
    }

    public void updateTitle(String categoryName) {
        toolbar.setTitle(categoryName);
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


    // { Work Around IF your press back and
    //      youtube thumbnail doesn't intalized yet

        boolean isBackPressed;
        @Override
        public void onBackPressed() {
            if(!thumbnailIntializing) {
                super.onBackPressed();
            } else {
                isBackPressed = true;
                return;
            }

        }

        boolean thumbnailIntializing = false;
        @Override
        public void onIntializationStart() {
            thumbnailIntializing = true;
        }

        @Override
        public void onIntializationComplete() {
            if(isBackPressed) {
                super.onBackPressed();
            }
            thumbnailIntializing = false;
        }

    // Work Around IF your press back and youtube thumbnail doesn't intalized yet }
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
