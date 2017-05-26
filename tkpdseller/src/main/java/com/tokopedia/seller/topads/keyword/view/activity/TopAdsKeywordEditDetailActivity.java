package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailPositiveFragment;

/**
 * @author sebastianuskh on 5/23/17.
 */

public class TopAdsKeywordEditDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_keyword_edit_detail);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(TopAdsKeywordEditDetailPositiveFragment.TAG);
        if (fragment == null) {
            fragment = TopAdsKeywordEditDetailPositiveFragment.createInstance();
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, TopAdsKeywordEditDetailPositiveFragment.TAG)
                    .commit();
        }
    }
}
