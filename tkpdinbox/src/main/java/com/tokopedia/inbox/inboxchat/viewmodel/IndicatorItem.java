package com.tokopedia.inbox.inboxchat.viewmodel;

/**
 * @author by nisie on 2/22/18.
 */

public class IndicatorItem {

    String title;
    int iconResId;
    boolean isActive;
    int notificationCount;

    public IndicatorItem(String title, int iconResId, boolean isActive) {
        this.title = title;
        this.iconResId = iconResId;
        this.isActive = isActive;
        this.notificationCount = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }
}
