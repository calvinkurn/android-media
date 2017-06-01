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
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupAdListFragment;
import com.tokopedia.seller.topads.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListActivity extends TActivity
        implements TopAdsAdListFragment.OnAdListFragmentListener {
    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_group_ad_list);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsGroupAdListFragment.createInstance(), TopAdsGroupAdListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsGroupAdListActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)){
            return;
        }
        if (showCaseDialog != null){
            return;
        }
        final TopAdsGroupAdListFragment topAdsGroupAdListFragment =
                (TopAdsGroupAdListFragment) getSupportFragmentManager().findFragmentByTag(TopAdsGroupAdListFragment.class.getSimpleName());
        if (topAdsGroupAdListFragment == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_group_list_title_1),
                            getString(R.string.topads_showcase_group_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 1.8), 0,width - (int)(height * 0.8), height}));

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_group_list_title_2),
                            getString(R.string.topads_showcase_group_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.9), 0,width, height}));

            RecyclerView recyclerView = topAdsGroupAdListFragment.getRecyclerView();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View itemView = topAdsGroupAdListFragment.getItemRecyclerView();
                    if (itemView != null) {
                        showCaseList.add(
                                new ShowCaseObject(
                                        itemView,
                                        getString(R.string.topads_showcase_group_list_title_3),
                                        getString(R.string.topads_showcase_group_list_desc_3),
                                        ShowCaseContentPosition.UNDEFINED,
                                        Color.WHITE));
                    }

                    FloatingActionButton fab = null;
                    if (fab != null) {
                        fab.show();
                        showCaseList.add(
                                new ShowCaseObject(
                                        fab,
                                        getString(R.string.topads_showcase_group_list_title_4),
                                        getString(R.string.topads_showcase_group_list_desc_4)));
                    }

                    showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                    showCaseDialog.show(TopAdsGroupAdListActivity.this, showCaseTag, showCaseList);
                }
            }, 300);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }));
        }

    }

}
