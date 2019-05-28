package com.tokopedia.discovery.newdiscovery.hotlist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.DaggerHotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.HotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.view.customview.DescriptionView;
import com.tokopedia.discovery.newdiscovery.hotlist.view.fragment.HotlistFragment;
import com.tokopedia.discovery.newdiscovery.hotlist.view.listener.AppBarState;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistContract;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistPresenter;
import com.tokopedia.discovery.newdiscovery.hotlist.view.listener.AppBarStateChangeListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.inject.Inject;

/**
 * Created by hangnadi on 9/26/17.
 */

public class HotlistActivity extends DiscoveryActivity
        implements HotlistContract.View {

    private static final String EXTRA_HOTLIST_PARAM_URL = "HOTLIST_URL";
    private static final String EXTRA_HOTLIST_PARAM_QUERY = "EXTRA_HOTLIST_PARAM_QUERY";
    private static final String EXTRA_HOTLIST_PARAM_ALIAS = "HOTLIST_ALIAS";
    private static final String EXTRA_HOTLIST_PARAM_TRACKER = "EXTRA_HOTLIST_PARAM_TRACKER";
    private static final String EXTRA_ACTIVITY_PAUSED = "EXTRA_ACTIVITY_PAUSED";
    private static final String TAG = HotlistActivity.class.getSimpleName();
    private AppBarLayout appBarLayout;
    private TextView descriptionTxt;
    private DescriptionView descriptionView;
    public FragmentListener fragmentListener;
    private CollapsingToolbarLayout toolbarLayout;

    @Inject
    HotlistPresenter hotlistPresenter;

    @DeepLink(Constants.Applinks.DISCOVERY_HOTLIST_DETAIL)
    public static Intent getCallingApplinkHostlistIntent(Context context, Bundle bundle) {
        return HotlistActivity.createInstanceUsingAlias(context,
                bundle.getString("alias", ""),
                bundle.getString("tracker_attribution", "")
        );
    }

    private static Intent createInstanceUsingAlias(Context context,
                                                   String alias,
                                                   String trackerAttribution) {
        Intent intent = new Intent(context, HotlistActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_HOTLIST_PARAM_ALIAS, alias);
        try {
            extras.putString(EXTRA_HOTLIST_PARAM_TRACKER, URLDecoder.decode(trackerAttribution, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            extras.putString(EXTRA_HOTLIST_PARAM_TRACKER, trackerAttribution.replaceAll("%20", " "));
        }
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createInstanceUsingAlias(Context context, String alias) {
        Intent intent = new Intent(context, HotlistActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_HOTLIST_PARAM_ALIAS, alias);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createInstanceUsingURL(Context context, String url, String searchQuery, boolean isActivityPaused) {
        Intent intent = new Intent(context, HotlistActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_HOTLIST_PARAM_URL, url);
        extras.putString(EXTRA_HOTLIST_PARAM_QUERY, searchQuery);
        extras.putBoolean(EXTRA_ACTIVITY_PAUSED, isActivityPaused);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(EXTRA_ACTIVITY_PAUSED, false)) {
            moveTaskToBack(true);
        }
        initInjector();
        setPresenter(hotlistPresenter);
        hotlistPresenter.attachView(this);
        hotlistPresenter.setDiscoveryView(this);
        inflateFragment();
        descriptionTxt = findViewById(R.id.description);
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, @AppBarState int state) {
                switch (state) {
                    case AppBarState.COLLAPSED:
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_thin);
                        if (searchItem != null)
                            searchItem.setIcon(R.drawable.search_icon);
                        break;
                    case AppBarState.EXPANDED:
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
                        if (searchItem != null)
                            searchItem.setIcon(R.drawable.ic_search_thin);
                        break;
                }
            }
        });

    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onSearchViewShown() {
        super.onSearchViewShown();
        if (fragmentListener != null) {
            fragmentListener.stopScroll();
        }
        appBarLayout.setVisibility(View.GONE);
        setSearchQuery(getToolbarTitle().toString());
    }

    @Override
    public void onSearchViewClosed() {
        super.onSearchViewClosed();
        appBarLayout.setVisibility(View.VISIBLE);
    }

    public void renderHotlistDescription(String txt) {
        descriptionView = new DescriptionView();
        descriptionView.setDescTxt(txt);
        descriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionView.show(getSupportFragmentManager(), TAG);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setIcon(R.drawable.search_icon);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        appBarLayout.setExpanded(true);
        setToolbarTitle(title.toString());
    }

    private void inflateFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, getHotlistFragment(), HotlistFragment.class.getSimpleName())
                    .commit();
        }
    }

    private Fragment getHotlistFragment() {
        String url = getIntent().getExtras().getString(EXTRA_HOTLIST_PARAM_URL, "");
        String alias = getIntent().getExtras().getString(EXTRA_HOTLIST_PARAM_ALIAS, "");
        String searchQuery = getIntent().getExtras().getString(EXTRA_HOTLIST_PARAM_QUERY, "");
        String trackerAttribution = getIntent().getExtras().getString(EXTRA_HOTLIST_PARAM_TRACKER, "");
        if (!alias.isEmpty()) {
            return HotlistFragment.createInstanceUsingAlias(alias, trackerAttribution);
        }
        return HotlistFragment.createInstanceUsingURL(url, searchQuery);
    }

    private void initInjector() {
        HotlistComponent hotlistComponent = DaggerHotlistComponent.builder()
                .appComponent(getComponent())
                .build();
        hotlistComponent.inject(this);
    }

    private CharSequence getToolbarTitle() {
        if (toolbarLayout != null && toolbarLayout.getTitle() != null) {
            return toolbarLayout.getTitle();
        } else {
            return "";
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_hotlist;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbarLayout = findViewById(R.id.toolbar_layout);
    }

    @Override
    protected void setToolbarTitle(String query) {
        if (getSupportActionBar() != null && query != null) {
            getSupportActionBar().setTitle(query);
            toolbarLayout.setTitle(query);
        }
    }

    @Override
    protected void onDestroy() {
        hotlistPresenter.detachView();
        super.onDestroy();
    }

    public interface FragmentListener {
        void stopScroll();
    }
}
