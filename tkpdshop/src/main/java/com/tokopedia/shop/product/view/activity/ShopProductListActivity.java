package com.tokopedia.shop.product.view.activity;

import android.app.Activity;
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
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    private String shopId;
    private String keyword;
    private ShopComponent component;
    private String etalaseId;
    private String etalaseName;
    private String sort;
    private String page;

    public static Intent createIntent(Context context, String shopId, String keyword, String etalaseId, String etalaseName) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.KEYWORD_EXTRAS, keyword);
        intent.putExtra(ShopParamConstant.ETALASE_ID, etalaseId);
        intent.putExtra(ShopParamConstant.ETALASE_NAME, etalaseName);
        return intent;
    }


    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
        return intent;
    }

    public static Intent createIntent(Activity activity, String shopId, String keyword, String id, String sort, String page) {
        Intent intent = createIntent(activity, shopId, keyword, id, "");
        intent.putExtra(ShopParamConstant.SORT, sort);
        intent.putExtra(ShopParamConstant.PAGE, page);
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
        shopId = getIntent().getStringExtra(ShopParamConstant.SHOP_ID);
        keyword = getIntent().getStringExtra(ShopParamConstant.KEYWORD_EXTRAS);
        etalaseId = getIntent().getStringExtra(ShopParamConstant.ETALASE_ID);
        etalaseName = getIntent().getStringExtra(ShopParamConstant.ETALASE_NAME);
        sort = getIntent().getStringExtra(ShopParamConstant.SORT);
        page = getIntent().getStringExtra(ShopParamConstant.PAGE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopProductListFragment.createInstance(shopId, keyword, etalaseId, etalaseName, sort, page);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }
}