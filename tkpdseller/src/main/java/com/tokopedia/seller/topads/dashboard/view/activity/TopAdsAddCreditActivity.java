package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsAddCreditActivity.class);
    }

    @DeepLink(Constants.Applinks.SellerApp.TOPADS_CREDIT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return getCallingIntent(context)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            Intent launchIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);
            if (launchIntent == null) {
                launchIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constants.URL_MARKET + GlobalConfig.PACKAGE_SELLER_APP)
                );
            } else {
                launchIntent.putExtra(Constants.EXTRA_APPLINK, extras.getString(DeepLink.URI));
            }
            return launchIntent;
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsAddCreditFragment.createInstance();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent = null;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(this);
            } else {
                homeIntent = HomeRouter.getHomeActivity(this);
            }
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}