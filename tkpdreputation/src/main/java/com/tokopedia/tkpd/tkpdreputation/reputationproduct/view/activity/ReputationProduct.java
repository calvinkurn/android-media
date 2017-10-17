package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.fragment.ProductReviewFragment;


/**
 * Created by hangnadi on 7/12/15.
 */
public class ReputationProduct extends TActivity {

    public static String ALL_TIME_REPUTATION = "all-time";
    public static String SIX_MONTH_REPUTATION = "six-month";

    @DeepLink(Constants.Applinks.PRODUCT_REPUTATION)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ReputationProduct.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_reputation_product);
        initView();
        initVariable();
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_REPUTATION_VIEW;
    }

    private void initVariable() {
        String productId = getIntent().getExtras().getString("product_id");
        String shopId = getIntent().getExtras().getString("shop_id");
        getFragmentManager().beginTransaction()
                .replace(R.id.container, ProductReviewFragment.createInstance(ALL_TIME_REPUTATION, productId, shopId))
                .commitAllowingStateLoss();
    }

    private void initView() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
