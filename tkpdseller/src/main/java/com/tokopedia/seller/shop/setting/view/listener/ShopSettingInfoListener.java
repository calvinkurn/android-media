package com.tokopedia.seller.shop.setting.view.listener;

/**
 * Created by zulfikarrahman on 3/23/17.
 */

public interface ShopSettingInfoListener {
    void onBrowseImageAction(ListenerOnImagePickerReady listenerOnImagePickerReady);

    interface ListenerOnImagePickerReady {
        void onImageReady(String uriPathImage);
    }
}
