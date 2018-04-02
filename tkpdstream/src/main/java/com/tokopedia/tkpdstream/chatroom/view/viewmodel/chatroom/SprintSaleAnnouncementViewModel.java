package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleAnnouncementViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final String SPRINT_SALE_UPCOMING = "flashsale_upcoming";
    public static final String SPRINT_SALE_START = "flashsale_start";
    public static final String SPRINT_SALE_END = "flashsale_end";

    private ArrayList<SprintSaleProductViewModel> listProducts;
    private String redirectUrl;
    private String campaignName;
    private long startDate;
    private long endDate;
    private String sprintSaleType;

    public SprintSaleAnnouncementViewModel(long createdAt, long updatedAt, String messageId,
                                           String senderId, String senderName, String senderIconUrl,
                                           boolean isInfluencer, boolean isAdministrator, String redirectUrl,
                                           ArrayList<SprintSaleProductViewModel> listProducts,
                                           String campaignName, long startDate, long endDate,
                                           String sprintSaleType) {
        super("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.redirectUrl = redirectUrl;
        this.listProducts = listProducts;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sprintSaleType = sprintSaleType;
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

    public String getCampaignName() {
        return campaignName;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public String getSprintSaleType() {
        return sprintSaleType;
    }
}