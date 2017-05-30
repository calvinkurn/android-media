package com.tokopedia.seller.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailFragmentListener;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailPositiveFragment;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/23/17.
 */

public class TopAdsKeywordEditDetailPositiveActivity extends BaseActivity implements HasComponent<AppComponent>, TopAdsKeywordEditDetailFragmentListener {

    public static final String KEYWORD_DETAIL_MODEL = "KEYWORD_DETAIL_MODEL";

    public static void start(Context context, KeywordAd model) {
        context.startActivity(createIntent(context, model));
    }

    public static Intent createIntent(Context context, KeywordAd model){
        Intent starter = new Intent(context, TopAdsKeywordEditDetailPositiveActivity.class);
        starter.putExtra(KEYWORD_DETAIL_MODEL, model);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_keyword_edit_detail);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(TopAdsKeywordEditDetailFragment.TAG);
        if (fragment == null) {
            KeywordAd model = getIntent().getParcelableExtra(KEYWORD_DETAIL_MODEL);
            fragment = getFragment(model);
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, TopAdsKeywordEditDetailFragment.TAG)
                    .commit();
        }
    }

    protected TopAdsKeywordEditDetailFragment getFragment(KeywordAd model) {
        return TopAdsKeywordEditDetailPositiveFragment.createInstance(model);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onSuccessEditTopAdsKeywordDetail(KeywordAd viewModel) {
        setResult(Activity.RESULT_OK, new Intent().putExtra(KEYWORD_DETAIL_MODEL, viewModel));
        finish();
    }
}
