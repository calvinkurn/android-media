package com.tokopedia.seller.shopsettings.edit.presenter;

import android.view.View;

import com.tokopedia.core.presenter.BaseView;

/**
 * Created by Toped10 on 5/19/2016.
 */
public interface ShopEditorView extends BaseView {

    void initView();
    void initViewInstance();
    void setData(int type, Object... data);
    void setOpenShop();
    void setCloseShop(String closeEnd);
    void setCloseShopWithSchedule(String closeEnd);
    void setShopIsGold(String until);
    void setShopReguler();
    Object getData(int type);
    void showDialog();
    void showToast(String message);
    void hideSoftInputWindow();
    void hideProgress();
    void showProgress();
    void hideAvaImage();
    void showAvaImage();
    void hideDialog();
    void hideButttonSend();
    void showButtonSend();
    void showShopEditor();
    void loadImageAva(String mShopAvaUri, int ic_default_shop_ava);
    void loadImageAva(String url);
    void showDialogNormal();
    void finishActivity();
    void hideShopEditor();
    void initAnalytics();
    void uploadImage(String data);

    void deleteShopCachev2();

    View getRootView();
}
