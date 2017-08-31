package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsDetailGroupActivity extends TActivity
        implements TopAdsDetailGroupFragment.OnTopAdsDetailGroupListener {

    private ShowCaseDialog showCaseDialog;

    public static final String TAG = TopAdsDetailGroupFragment.class.getSimpleName();

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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailGroupFragment.createInstance(ad, adId);
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDetailGroupActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)) {
            return;
        }
        if (showCaseDialog!= null) {
            return;
        }

        final TopAdsDetailGroupFragment topAdsDetailGroupFragment =
                (TopAdsDetailGroupFragment) getSupportFragmentManager().findFragmentByTag(TopAdsDetailGroupFragment.class.getSimpleName());

        if (topAdsDetailGroupFragment == null || topAdsDetailGroupFragment.getView() == null) {
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
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OneUseGlobalLayoutListener(toolbar, new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowCase();
                }
            }));
        }
    }


    public static Intent createIntent(Context context, String groupId) {
        Intent intent = new Intent(context, TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        return intent;
    }
}
