package com.tokopedia.seller.product.domain.listener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductNotificationListener {
    void createNotification(String productName);

    void notificationUpdate(int stepNotification);
}
