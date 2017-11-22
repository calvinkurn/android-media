package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailProductFragment;
import com.tokopedia.topads.dashboard.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsDetailProductActivity extends BaseSimpleActivity implements TopAdsDetailProductFragment.TopAdsDetailProductFragmentListener {

    public static final String TAG = TopAdsDetailProductFragment.class.getSimpleName();

    private ShowCaseDialog showCaseDialog;

    @DeepLink(Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            String userId = extras.getString("user_id", "");
            if (!TextUtils.isEmpty(userId)) {
                if (SessionHandler.getLoginID(context).equalsIgnoreCase(userId)) {
                    Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                    return getCallingIntent(context, extras.getString("ad_id", ""))
                            .setData(uri.build())
                            .putExtras(extras);
                } else {
                    return TopAdsDashboardActivity.getCallingIntent(context)
                            .putExtras(extras);
                }
            } else {
                Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                return getCallingIntent(context, extras.getString("ad_id", ""))
                        .setData(uri.build())
                        .putExtras(extras);
            }
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent getCallingIntent(Context activity, String adsId) {
        Intent intent = new Intent(activity, TopAdsDetailProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adsId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            ProductAd ad = null;
            String adId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsDetailProductFragment.createInstance(ad, adId);
            return fragment;
        }
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
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
            String deepLink = getIntent().getStringExtra(DeepLink.URI);
            if(deepLink.contains(Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL)) {
                super.onBackPressed();
            } else {
                Intent intent = new Intent(this, TopAdsDashboardActivity.class);
                this.startActivity(intent);
                this.finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDetailProductActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)) {
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

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                            .withCustomTarget(new int[]{width - (int) (height * 0.8), 0, width, height}));
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
        } else {
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
