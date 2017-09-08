package com.tokopedia.seller.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment;

import java.util.List;

import static com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsGroupNewPromoActivity extends TActivity {

    @DeepLink(Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            String userId = extras.getString("user_id", "");
            if (!TextUtils.isEmpty(userId)) {
                if (SessionHandler.getLoginID(context).equalsIgnoreCase(userId)) {
                    Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                    return getCallingIntent(context)
                            .setData(uri.build())
                            .putExtras(extras);
                } else {
                    return TopAdsDashboardActivity.getCallingIntent(context)
                            .putExtras(extras);
                }
            } else {
                Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                return getCallingIntent(context)
                        .setData(uri.build())
                        .putExtras(extras);
            }
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsGroupNewPromoActivity.class);
    }

    // from deeplink
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        initFromIntent();
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsGroupNewPromoFragment.createInstance(itemId), TopAdsGroupNewPromoFragment.class.getSimpleName())
                .commit();
    }

    private void initFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            itemId = intent.getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AD_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // top ads new groups/edit existing group/promo not in group has been success
                Intent intent = new Intent();
                boolean adStatusChanged = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
                if (adStatusChanged) {
                    intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            //coming from deeplink
            String deepLink = getIntent().getStringExtra(DeepLink.URI);
            if(deepLink.contains(Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE)) {
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
}