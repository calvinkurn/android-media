package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;

import rx.functions.Action1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class NotificationManager {

    private final UpdateNotification updateNotification;

    public NotificationManager(AddProductNotificationListener listener, long productId, String productName) {
        listener.createNotification(String.valueOf(productId), productName);
        updateNotification = new UpdateNotification(listener, String.valueOf(productId));
    }

    private class UpdateNotification implements Action1<Object> {
        private final AddProductNotificationListener listener;
        private final String productId;

        public UpdateNotification(AddProductNotificationListener listener, String productId) {
            this.listener = listener;
            this.productId = productId;
        }

        @Override
        public void call(Object o) {
            if (listener != null) {
                listener.notificationUpdate(productId);
            }
        }
    }

    public UpdateNotification getUpdateNotification() {
        return updateNotification;
    }
}
