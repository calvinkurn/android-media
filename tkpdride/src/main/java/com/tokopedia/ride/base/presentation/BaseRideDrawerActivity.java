package com.tokopedia.ride.base.presentation;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alvarisi on 3/13/17.
 */

public abstract class BaseRideDrawerActivity extends BaseActivity implements DrawerMenuView.ActionListener {
    DrawerMenuView mMenuView;

    DrawerLayout drawerLayout;
    FrameLayout navDrawer;
    Toolbar mToolbar;
    private Unbinder unbinder;

    public BaseRideDrawerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_uber_drawer);
        unbinder = ButterKnife.bind(this);
        if (mToolbar != null)
        setSupportActionBar(mToolbar);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content_root_frameLayout);
        LayoutInflater.from(this).inflate(getLayoutId(), viewGroup, true);

        setupDrawerMenu();
    }

    private void setupDrawerMenu() {
        mMenuView = new DrawerMenuView(this, this);
        navDrawer.addView(mMenuView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawerLayout != null) drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onHomeSelected() {

    }

    protected abstract int getLayoutId();
}
