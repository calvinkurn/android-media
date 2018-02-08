
package com.tokopedia.seller.shopsettings.edit.presenter;

import android.content.Intent;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.core.shop.model.ShopScheduleModel;

/**
 * Created by Toped10 on 5/19/2016.
 */
public abstract class ShopEditorPresenter extends BaseImpl<ShopEditorView>{
    public static final int FragmentId = 1_192_129;
    public static final String DATA = "ShopEditor_DATA";

    public static final int SHOP_NAME = 1;
    public static final int SHOP_SLOGAN = 2;
    public static final int SHOP_DESC = 3;
    public static final int SHOP_NAME_ERROR = 4;
    public static final int SHOP_SLOGAN_ERROR = 5;
    public static final int SHOP_DESC_ERROR = 6;


    public ShopEditorPresenter(ShopEditorView view) {
        super(view);
    }

    public abstract void sendDataShop();
    public abstract void onClickCloseShop(ShopEditorPresenter presenter);
    public abstract void uploadUpdateImage(Intent data);
    public abstract void uploadUpdateImage(String data);
    public abstract void updateShopSchedule(ShopScheduleModel shopScheduleModel);

    public abstract void getShopData();
}
