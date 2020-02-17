package com.tokopedia.seller.seller.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.seller.info.view.fragment.SellerInfoWebViewFragment;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoWebViewActivity extends BaseSimpleActivity{

    public static final String KEY_APPLINK_URL = "url";

    // example url: tokopedia://sellerinfo/detail?url=http%3A%2F%2Ftkp.me%2Fta43
    @DeepLink({Constants.Applinks.SELLER_INFO_DETAIL})
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        Intent homeIntent;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivityInterfaceRouter(context);
        }

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);

        Intent parentIntent = new Intent(context, SellerInfoActivity.class);
        taskStackBuilder.addNextIntent(parentIntent);

        String url = extras.getString(KEY_APPLINK_URL, "");
        if (!TextUtils.isEmpty(url)) {
            Intent detailsIntent = getCallingIntent(context, url);
            taskStackBuilder.addNextIntent(detailsIntent);
        }
        return taskStackBuilder;
    }

    public static Intent getCallingIntent(Context context, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseSessionWebViewFragment.ARGS_URL, url);
        Intent intent = new Intent(context, SellerInfoWebViewActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    private String extraUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extraUrl = getIntent().getStringExtra(BaseSessionWebViewFragment.ARGS_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return SellerInfoWebViewFragment.newInstance(extraUrl);
    }


}
