package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;

import rx.functions.Action1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class NotificationManager {

    private final UpdateNotification updateNotification;

    public NotificationManager(AddProductNotificationListener listener, long draftProductId, String productName) {
        listener.createNotification(draftProductId, productName);
        updateNotification = new UpdateNotification(listener, draftProductId);
    }

    private class UpdateNotification implements Action1<Object> {
        private final AddProductNotificationListener listener;
        private final long productId;

        public UpdateNotification(AddProductNotificationListener listener, long draftProductId) {
            this.listener = listener;
            this.productId = draftProductId;
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
