package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerChildViewModel {
    private String partnerAvatar;
    private String partnerName;
    private String partnerUrl;

    public ChannelPartnerChildViewModel(String partnerAvatar, String partnerName,
                                        String partnerUrl) {
        this.partnerAvatar = partnerAvatar;
        this.partnerName = partnerName;
        this.partnerUrl = partnerUrl;
    }

    public String getPartnerAvatar() {
        return partnerAvatar;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerUrl() {
        return partnerUrl;
    }
}
