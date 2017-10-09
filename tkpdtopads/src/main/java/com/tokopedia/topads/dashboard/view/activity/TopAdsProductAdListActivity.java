package com.tokopedia.topads.dashboard.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAdListFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductAdListFragment;
import com.tokopedia.topads.dashboard.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsProductAdListActivity extends TActivity
        implements TopAdsAdListFragment.OnAdListFragmentListener {

    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupAd groupAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_GROUP);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsProductAdListFragment.createInstance(groupAd), TopAdsProductAdListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsProductAdListActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)){
            return;
        }
        if (showCaseDialog != null) {
            return;
        }
        final TopAdsProductAdListFragment topAdsProductAdListFragment =
                (TopAdsProductAdListFragment) getSupportFragmentManager().findFragmentByTag(TopAdsProductAdListFragment.class.getSimpleName());
        if (topAdsProductAdListFragment == null || topAdsProductAdListFragment.getView() == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int height = toolbar.getHeight();
            int width = toolbar.getWidth() ;

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_product_list_title_1),
                            getString(R.string.topads_showcase_product_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 1.8), 0,
                                    width - (int)(height * 0.8), height}));

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_product_list_title_2),
                            getString(R.string.topads_showcase_product_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.9), 0,width, height}));

            RecyclerView recyclerView = topAdsProductAdListFragment.getRecyclerView();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (topAdsProductAdListFragment.getView() == null) {
                        return;
                    }
                    View dateView = topAdsProductAdListFragment.getDateView();
                    if (dateView != null) {
                        dateView.setVisibility(View.VISIBLE);
                        showCaseList.add(
                                new ShowCaseObject(
                                        dateView,
                                        getString(R.string.topads_showcase_product_list_title_3),
                                        getString(R.string.topads_showcase_product_list_desc_3)));
                    }

                    View itemView = topAdsProductAdListFragment.getItemRecyclerView();
                    if (itemView != null) {
                        showCaseList.add(
                                new ShowCaseObject(
                                        itemView,
                                        getString(R.string.topads_showcase_product_list_title_4),
                                        getString(R.string.topads_showcase_product_list_desc_4),
                                        ShowCaseContentPosition.UNDEFINED,
                                        Color.WHITE));
                    }

                    showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                    showCaseDialog.show(TopAdsProductAdListActivity.this, showCaseTag, showCaseList);
                }
            }, 300);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(toolbar, new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowCase();
                }
            }));
        }

    }
}
