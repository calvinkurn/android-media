package com.tokopedia.seller.topads.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductAdListFragment;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
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
        getFragmentManager().beginTransaction().disallowAddToBackStack()
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
//        if (ShowCasePreference.hasShown(this, showCaseTag)){
//            return;
//        }
        if (showCaseDialog != null) {
            return;
        }
        final TopAdsProductAdListFragment topAdsProductAdListFragment =
                (TopAdsProductAdListFragment) getFragmentManager().findFragmentByTag(TopAdsProductAdListFragment.class.getSimpleName());
        if (topAdsProductAdListFragment == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int radius = toolbar.getHeight() / 2;

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_product_list_title_1),
                            getString(R.string.topads_showcase_product_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{toolbar.getWidth() - (int) (radius * 2.6), radius}
                                    , radius));

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_product_list_title_2),
                            getString(R.string.topads_showcase_product_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{toolbar.getWidth() -(int) (radius * 0.85), radius}
                                    , radius));

            RecyclerView recyclerView = topAdsProductAdListFragment.getRecyclerView();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View itemView = topAdsProductAdListFragment.getItemRecyclerView();
                    if (itemView != null) {
                        showCaseList.add(
                                new ShowCaseObject(
                                        itemView,
                                        getString(R.string.topads_showcase_product_list_title_3),
                                        getString(R.string.topads_showcase_product_list_desc_3),
                                        ShowCaseContentPosition.UNDEFINED,
                                        Color.WHITE));
                    }

                    FloatingActionButton fab = topAdsProductAdListFragment.getFab();
                    if (fab != null) {
                        fab.show();
                        showCaseList.add(
                                new ShowCaseObject(
                                        fab,
                                        getString(R.string.topads_showcase_product_list_title_4),
                                        getString(R.string.topads_showcase_product_list_desc_4)));
                    }

                    showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                    showCaseDialog.show(TopAdsProductAdListActivity.this, showCaseTag, showCaseList);
                }
            }, 300);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowCase();
                    if (Build.VERSION.SDK_INT < 16) {
                        toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }

    }
}
