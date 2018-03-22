package com.tokopedia.tkpdstream.chatroom.view.viewmodel.tab;

/**
 * @author by nisie on 3/21/18.
 */

public class TabViewModel {
    private String title;
    private boolean isActive;
    private boolean isUpdated;

    public TabViewModel(String title) {
        this.title = title;
        this.isActive = false;
        this.isUpdated = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
