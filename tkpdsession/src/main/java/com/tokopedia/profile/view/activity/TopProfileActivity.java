package com.tokopedia.profile.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.design.tab.Tabs;
import com.tokopedia.profile.view.adapter.ProfileTabPagerAdapter;
import com.tokopedia.profile.view.fragment.TopProfileFragment;
import com.tokopedia.profile.view.viewmodel.ProfileSectionItem;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileActivity extends BaseEmptyActivity implements HasComponent {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private BaseDaggerFragment kolPostFragment;

    private static final String TITLE_PROFILE = "Info Akun";
    private static final String TITLE_POST = "Post";

    private Tabs tabLayout;
    private ViewPager viewPager;

    public static Intent newInstance(Context context) {
        return new Intent(context, TopProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationContext() instanceof SessionRouter) {
            //TODO milhamj change this userid
            kolPostFragment = ((SessionRouter) getApplicationContext()).getKolPostFragment("6543110");
        }
        inflateFragment();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        tabLayout =findViewById(R.id.tab_profile);
        viewPager = findViewById(R.id.pager);
        setupToolbar();
        loadSection();
    }

    //TODO milhamj remove this func
    private void inflateFragment() {
        if (kolPostFragment != null) {
            String TAG = "LOL";
            if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,
                                getSupportFragmentManager().findFragmentByTag(TAG))
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, kolPostFragment, TAG)
                        .commit();
            }
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top_profile;
    }

    @Override
    public Object getComponent() {
        //TODO milhamj
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Kucing");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_back_button);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void loadSection(){
        List<ProfileSectionItem> profileSectionItemList = new ArrayList<>();

        //TODO add content fragment
        TopProfileFragment profileFragment = TopProfileFragment.newInstance();
        profileSectionItemList.add(new ProfileSectionItem(TITLE_PROFILE, profileFragment));

        ProfileTabPagerAdapter profileTabPagerAdapter = new ProfileTabPagerAdapter(getSupportFragmentManager());
        profileTabPagerAdapter.setItemList(profileSectionItemList);
        viewPager.setAdapter(profileTabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
