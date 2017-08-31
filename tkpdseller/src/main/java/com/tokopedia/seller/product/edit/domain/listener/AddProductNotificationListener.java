package com.tokopedia.seller.product.edit.domain.listener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductNotificationListener {
    void createNotification(String productDraftId, String productName);

    void notificationUpdate(String productDraftId);
}
