package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailPositiveFragment;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * @author sebastianuskh on 5/23/17.
 */

public class TopAdsKeywordEditDetailActivity extends BaseActivity {

    public static final String KEYWORD_DETAIL_MODEL = "KEYWORD_DETAIL_MODEL";

    public static void start(Context context, TopAdsKeywordEditDetailViewModel model) {
        context.startActivity(createIntent(context, model));
    }

    public static Intent createIntent(Context context, TopAdsKeywordEditDetailViewModel model){
        Intent starter = new Intent(context, TopAdsKeywordEditDetailActivity.class);
        starter.putExtra(KEYWORD_DETAIL_MODEL, model);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_keyword_edit_detail);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(TopAdsKeywordEditDetailPositiveFragment.TAG);
        if (fragment == null) {
            TopAdsKeywordEditDetailViewModel model = getIntent().getParcelableExtra(KEYWORD_DETAIL_MODEL);
            fragment = TopAdsKeywordEditDetailPositiveFragment.createInstance(model);
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, TopAdsKeywordEditDetailPositiveFragment.TAG)
                    .commit();
        }
    }
}
