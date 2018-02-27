package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.network.entity.topads.TopAds;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment;

import static com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsGroupNewPromoActivity extends BaseSimpleActivity {

    public static final String PARAM_ITEM_ID = "item_id";
    public static final String PARAM_USER_ID = "user_id";

    @DeepLink(Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            String userId = extras.getString(PARAM_USER_ID, "");
            if (!TextUtils.isEmpty(userId)) {
                if (SessionHandler.getLoginID(context).equalsIgnoreCase(userId)) {
                    Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                    return getCallingIntent(context)
                            .setData(uri.build())
                            .putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, uri.build().getQueryParameter(PARAM_ITEM_ID))
                            .putExtras(extras);
                } else {
                    return TopAdsDashboardActivity.getCallingIntent(context)
                            .putExtras(extras);
                }
            } else {
                Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
                return getCallingIntent(context)
                        .setData(uri.build())
                        .putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, uri.build().getQueryParameter(PARAM_ITEM_ID))
                        .putExtras(extras);
            }
        } else {
            Intent launchIntent = ApplinkUtils.getSellerAppApplinkIntent(context, extras);
            Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
            String itemId = uri.getQueryParameter(PARAM_ITEM_ID);
            launchIntent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemId);
            return launchIntent;
        }
    }

    public static Intent createIntent(Context context, String itemId, String source){
        Intent intent = getCallingIntent(context);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_SOURCE, source);
        return intent;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsGroupNewPromoActivity.class);
    }

    // from deeplink
    String itemId;
    String source;

    private void initFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            itemId = intent.getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
            source = intent.getStringExtra(TopAdsExtraConstant.EXTRA_SOURCE);
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
            if(!TextUtils.isEmpty(deepLink) && deepLink.contains(Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE)) {
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
    protected Fragment getNewFragment() {
        initFromIntent();
        return TopAdsGroupNewPromoFragment.createInstance(itemId, source);
    }

    @Override
    protected String getTagFragment() {
        return TopAdsGroupNewPromoFragment.class.getSimpleName();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}