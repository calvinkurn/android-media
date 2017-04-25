package com.tokopedia.seller.topads.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsDetailGroupActivity extends TActivity
        implements TopAdsDetailGroupFragment.OnTopAdsDetailGroupListener {

    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_group);

        GroupAd ad = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailGroupFragment.createInstance(ad, adId), TopAdsDetailGroupFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDetailGroupActivity.class.getName();
//        if (ShowCasePreference.hasShown(this, showCaseTag)) {
//            return;
//        }
        if (showCaseDialog!= null) {
            return;
        }

        final TopAdsDetailGroupFragment topAdsDetailGroupFragment =
                (TopAdsDetailGroupFragment) getFragmentManager().findFragmentByTag(TopAdsDetailGroupFragment.class.getSimpleName());

        if (topAdsDetailGroupFragment == null) {
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
                            getString(R.string.topads_showcase_detail_group_title_1),
                            getString(R.string.topads_showcase_detail_group_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.8), 0,width, height}));
            View statusView = topAdsDetailGroupFragment.getStatusView();
            if (statusView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                statusView,
                                getString(R.string.topads_showcase_detail_group_title_2),
                                getString(R.string.topads_showcase_detail_group_desc_2),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }

            View productView = topAdsDetailGroupFragment.getProductView();
            if (productView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                productView,
                                getString(R.string.topads_showcase_detail_group_title_3),
                                getString(R.string.topads_showcase_detail_group_desc_3),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }

            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
            showCaseDialog.show(TopAdsDetailGroupActivity.this, showCaseTag, showCaseList);
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
