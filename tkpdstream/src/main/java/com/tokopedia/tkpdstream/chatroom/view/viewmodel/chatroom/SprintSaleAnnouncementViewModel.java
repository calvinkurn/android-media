package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleAnnouncementViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final String SPRINT_SALE = "flashsale_add";
    private ArrayList<SprintSaleProductViewModel> listProducts;
    private String redirectUrl;
    private boolean isActive;
    private String campaignName;
    private long startDate;
    private long endDate;

    public SprintSaleAnnouncementViewModel(long createdAt, long updatedAt, String messageId,
                                           String senderId, String senderName, String senderIconUrl,
                                           boolean isInfluencer, boolean isAdministrator, String redirectUrl,
                                           ArrayList<SprintSaleProductViewModel> listProducts, boolean isActive,
                                           String campaignName, long startDate, long endDate,
                                           boolean canVibrate) {
        super("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator, canVibrate);
        this.redirectUrl = redirectUrl;
        this.listProducts = listProducts;
        this.isActive = isActive;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public ArrayList<SprintSaleProductViewModel> getListProducts() {
        return listProducts;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }
}