package com.tokopedia.discovery.newdiscovery.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.common.model.SearchParameter;

import java.util.List;

public class DiscoveryActivity extends BaseDiscoveryActivity implements
        BottomNavigationListener {

    private Toolbar toolbar;
    private FrameLayout container;
    private AHBottomNavigation bottomNavigation;
    protected ProgressBar loadingView;

    public MenuItem searchItem;

    protected TkpdProgressDialog tkpdProgressDialog;

    protected View root;

    protected SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        proceed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        proceed();
    }

    private void proceed() {
        initView();
        prepareView();
    }

    protected int getLayoutRes() {
        return R.layout.activity_base_discovery;
    }

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.container);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        loadingView = findViewById(R.id.progressBar);
        root = findViewById(R.id.root);
    }

    protected void prepareView() {
        initToolbar();
        showLoadingView(false);
    }

    protected void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setOnClickListener(v -> RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE));
    }

    protected void setToolbarTitle(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
    }

    public void adjustViewContainer(int y) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) container.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, y);
        container.setLayoutParams(layoutParams);
    }

    @Override
    public void setupBottomNavigation(List<AHBottomNavigationItem> items,
                                      AHBottomNavigation.OnTabSelectedListener tabSelectedListener) {

        bottomNavigation.setBackgroundResource(R.drawable.bottomtab_background);
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.tkpd_dark_green));
        bottomNavigation.setOnTabSelectedListener(tabSelectedListener);
        bottomNavigation.setUseElevation(true, getResources().getDimension(R.dimen.bottom_navigation_elevation));
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                adjustViewContainer(y);
            }
        });
    }

    @Override
    public void showBottomNavigation() {
        bottomNavigation.restoreBottomNavigation(true);
    }

    @Override
    public void hideBottomNavigation() {
        if (bottomNavigation != null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    @Override
    public void enableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(true);
        }
    }

    @Override
    public void disableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(false);
        }
    }

    @Override
    public void refreshBottomNavigationIcon(List<AHBottomNavigationItem> items) {
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
    }

    @Override
    public void onHandleResponseError() {

        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
        showLoadingView(false);
        showContainer(true);
        NetworkErrorHelper.showEmptyState(this, container, null);
    }
}
