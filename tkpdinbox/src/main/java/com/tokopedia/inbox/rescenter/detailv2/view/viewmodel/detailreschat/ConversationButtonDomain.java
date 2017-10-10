package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationButtonDomain {

    private int trackAwb;
    private int editAwb;
    private int editAddress;

    public ConversationButtonDomain(int trackAwb, int editAwb, int editAddress) {
        this.trackAwb = trackAwb;
        this.editAwb = editAwb;
        this.editAddress = editAddress;
    }

    public int getTrackAwb() {
        return trackAwb;
    }

    public void setTrackAwb(int trackAwb) {
        this.trackAwb = trackAwb;
    }

    public int getEditAwb() {
        return editAwb;
    }

    public void setEditAwb(int editAwb) {
        this.editAwb = editAwb;
    }

    public int getEditAddress() {
        return editAddress;
    }

    public void setEditAddress(int editAddress) {
        this.editAddress = editAddress;
    }
}
