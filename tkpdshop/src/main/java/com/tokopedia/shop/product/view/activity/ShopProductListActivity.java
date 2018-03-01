package com.tokopedia.shop.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    public static final String KEYWORD_EXTRAS = "KEYWORD_EXTRAS";
    private String shopId;
    private String shopDomain;
    private String keyword;
    private ShopComponent component;

    public static Intent createIntent(Context context, String shopId, String keyword, String etalaseId, String etalaseName) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        intent.putExtra(KEYWORD_EXTRAS, keyword);
        intent.putExtra(ShopProductListFragment.ETALASE_ID, etalaseId);
        intent.putExtra(ShopProductListFragment.ETALASE_NAME, etalaseName);
        return intent;
    }

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        return intent;
    }

    public static Intent createIntentWithDomain(Context context, String shopDomain) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @DeepLink(ShopAppLink.SHOP_ETALASE)
    public static Intent getCallingIntentEtalaseSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopProductListActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(SHOP_ID);
        shopDomain = getIntent().getStringExtra(SHOP_DOMAIN);
        keyword = getIntent().getStringExtra(KEYWORD_EXTRAS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopProductListFragment.createInstance(shopId, keyword);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }
}
