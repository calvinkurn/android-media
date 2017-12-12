package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.seller.shop.ShopEditorActivity;

/**
 * Created by nakama on 12/12/17.
 */

public class TkpdSeller {
    public static void goToCreateShop(Context context){
        Intent intent = new Intent(context, ShopEditorActivity.class);
        intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
        intent.putExtra(SellerRouter.ShopSettingConstant.ON_BACK,
                SellerRouter.ShopSettingConstant.LOG_OUT);
        context.startActivity(intent);
    }
}
