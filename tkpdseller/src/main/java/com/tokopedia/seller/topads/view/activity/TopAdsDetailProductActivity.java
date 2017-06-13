package com.tokopedia.seller.topads.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.ProductAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;
import com.tokopedia.seller.topads.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsDetailProductActivity extends TActivity implements TopAdsDetailProductFragment.TopAdsDetailProductFragmentListener {

    public static final String TAG = TopAdsDetailProductFragment.class.getSimpleName();

    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_product);

        ProductAd ad = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailProductFragment.createInstance(ad, adId);
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment,
                        TAG)
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void goToProductActivity(String productUrl) {
        if (getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getApplication()).goToProductDetail(this, productUrl);
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            //coming from deeplink
            Intent intent = new Intent(this, TopAdsDashboardActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDetailProductActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)){
            return;
        }
        if (showCaseDialog != null) {
            return;
        }

        final TopAdsDetailProductFragment topAdsDetailProductFragment =
                (TopAdsDetailProductFragment) getSupportFragmentManager().findFragmentByTag(TopAdsDetailProductFragment.class.getSimpleName());

        if (topAdsDetailProductFragment == null) {
            return;
        }

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();
            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_detail_promo_title_1),
                            getString(R.string.topads_showcase_detail_promo_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.8), 0,width, height}));
            View statusView = topAdsDetailProductFragment.getStatusView();
            if (statusView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                statusView,
                                getString(R.string.topads_showcase_detail_promo_title_2),
                                getString(R.string.topads_showcase_detail_promo_desc_2),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }
            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
            showCaseDialog.show(TopAdsDetailProductActivity.this, showCaseTag, showCaseList);
        }
        else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(
                    toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }
            ));
        }

    }
}
