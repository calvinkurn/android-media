package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import java.util.List;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerViewModel {
    private String partnerTitle;
    private List<ChannelPartnerChildViewModel> child;

    public ChannelPartnerViewModel(String partnerTitle, List<ChannelPartnerChildViewModel> child) {
        this.partnerTitle = partnerTitle;
        this.child = child;
    }

    public String getPartnerTitle() {
        return partnerTitle;
    }

    public List<ChannelPartnerChildViewModel> getChild() {
        return child;
    }
}
