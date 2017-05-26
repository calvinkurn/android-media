package com.tokopedia.seller.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.AbsTopAdsKeywordAddSummaryFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddDetailFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddNegativeSummaryFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddPositiveSummaryFragment;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordAddSummaryActivity extends BaseActivity{

    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_GROUP_ID = "grp_id";
    public static final String EXTRA_IS_POSITIVE= "is_positive";

    public static void start(Activity activity, int requestCode,
                             int groupId, String groupName, boolean isPositive) {
        Intent intent = createIntent(activity, groupId, groupName, isPositive);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             int groupId, String groupName, boolean isPositive) {
        Intent intent = createIntent(context, groupId, groupName, isPositive);
        fragment.startActivityForResult(intent,requestCode);
    }

    private static Intent createIntent(Context context,
                                       int groupId, String groupName, boolean isPositive) {
        Intent intent = new Intent(context, TopAdsKeywordAddDetailFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GROUP_ID, groupId);
        bundle.putString(EXTRA_GROUP_NAME, groupName);
        bundle.putBoolean(EXTRA_IS_POSITIVE, isPositive);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        int groupId;
        String groupName;
        boolean isPositive;
        if (intent.getExtras() == null) {
            groupId = 16349;
            groupName = "Group 2";
            isPositive = true;
        } else {
            groupId = intent.getIntExtra(EXTRA_GROUP_ID, 0);
            groupName = intent.getStringExtra(EXTRA_GROUP_NAME);
            isPositive = intent.getBooleanExtra(EXTRA_IS_POSITIVE, true);
        }

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment;
            if (isPositive) {
                fragment = TopAdsKeywordAddPositiveSummaryFragment.newInstance(
                        groupId, groupName);
            } else {
                fragment = TopAdsKeywordAddNegativeSummaryFragment.newInstance(
                        groupId, groupName);
            }
            fragmentTransaction.replace(R.id.container, fragment, AbsTopAdsKeywordAddSummaryFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                super. onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
