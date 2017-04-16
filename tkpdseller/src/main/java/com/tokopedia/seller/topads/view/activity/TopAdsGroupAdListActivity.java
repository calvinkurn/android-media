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
        implements TopAdsAdListFragment.OnAdListFragmentListener{
    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_group_ad_list);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
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
        if (! ShowCasePreference.hasShown(this, showCaseTag) &&
                showCaseDialog == null) {
            final TopAdsGroupAdListFragment topAdsGroupAdListFragment =
                    (TopAdsGroupAdListFragment) getFragmentManager().findFragmentByTag(TopAdsGroupAdListFragment.class.getSimpleName());
            if (topAdsGroupAdListFragment!= null) {

                final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
                if (toolbar.getHeight() > 0) {
                    final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
                    int radius = toolbar.getHeight() / 2;

                    showCaseList.add(
                            new ShowCaseObject(
                                    findViewById(android.R.id.content),
                                    getString(R.string.showcase_topads_group_list_title_1),
                                    getString(R.string.showcase_topads_group_list_desc_1),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE)
                                    .withCustomTarget(new int[]{ toolbar.getWidth() - radius*5/2 , radius}
                                            , radius) );

                    showCaseList.add(
                            new ShowCaseObject(
                                    findViewById(android.R.id.content),
                                    getString(R.string.showcase_topads_group_list_title_2),
                                    getString(R.string.showcase_topads_group_list_desc_2),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE)
                                    .withCustomTarget(new int[]{ toolbar.getWidth() - radius*4/5 , radius}
                                            , radius) );

                    RecyclerView recyclerView = topAdsGroupAdListFragment.getRecyclerView();
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View itemView = topAdsGroupAdListFragment.getItemRecyclerView();
                            if (itemView!= null) {
                                showCaseList.add(
                                        new ShowCaseObject(
                                                itemView,
                                                getString(R.string.showcase_topads_group_list_title_3),
                                                getString(R.string.showcase_topads_group_list_desc_3),
                                                ShowCaseContentPosition.UNDEFINED,
                                                Color.WHITE));
                            }

                            FloatingActionButton fab = topAdsGroupAdListFragment.getFab();
                            if (fab!= null) {
                                fab.show();
                                showCaseList.add(
                                        new ShowCaseObject(
                                                fab,
                                                getString(R.string.showcase_topads_group_list_title_4),
                                                getString(R.string.showcase_topads_group_list_desc_4)));
                            }

                            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                            showCaseDialog.show(TopAdsGroupAdListActivity.this, showCaseTag, showCaseList);
                        }
                    },300);

                }
                else {
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

    }

}
