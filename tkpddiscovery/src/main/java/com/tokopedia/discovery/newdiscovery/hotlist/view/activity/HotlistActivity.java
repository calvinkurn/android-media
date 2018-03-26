package com.tokopedia.discovery.newdiscovery.hotlist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.DaggerHotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.HotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.view.fragment.HotlistFragment;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistContract;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistPresenter;

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
        extras.putString(EXTRA_HOTLIST_PARAM_TRACKER, trackerAttribution);
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

    public static Intent createInstanceUsingURL(Context context, String url, String searchQuery) {
        Intent intent = new Intent(context, HotlistActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_HOTLIST_PARAM_URL, url);
        extras.putString(EXTRA_HOTLIST_PARAM_QUERY, searchQuery);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        setPresenter(hotlistPresenter);
        hotlistPresenter.attachView(this);
        hotlistPresenter.setDiscoveryView(this);
        inflateFragment();
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

    @Override
    protected void onDestroy() {
        hotlistPresenter.detachView();
        super.onDestroy();
    }
}
