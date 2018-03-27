package com.tokopedia.tkpdstream.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author by StevenFredian on 05/03/18.
 */

public class StreamAnalytics {
    private AnalyticTracker analyticTracker;

    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String TRACKER_ATTRIBUTION = "tracker_attribution";

    private static final String EVENT_CATEGORY_GROUPCHAT_LIST = "groupchat";
    private static final String EVENT_CATEGORY_GROUPCHAT_ROOM = "groupchat room";
    private static final String EVENT_CATEGORY_SHARE = "share page";
    public static final String EVENT_CATEGORY_LEFT_NAVIGATION = "left navigation";


    private static final String EVENT_ACTION_GROUPCHAT_LIST = "click on group chat list";
    private static final String EVENT_ACTION_VOTE = "click on vote";
    private static final String EVENT_ACTION_SHARE = "click on share";
    private static final String EVENT_ACTION_SHARE_CHANNEL = "click share channel";
    private static final String EVENT_ACTION_JOIN_VOTE_NOW = "click on join";
    private static final String EVENT_ACTION_CLICK_THUMBNAIL = "click on image thumbnail";
    private static final String EVENT_ACTION_CLICK_VOTE_COMPONENT = "click on component - ";
    private static final String EVENT_ACTION_CLICK_VOTE_EXPAND = "click on vote expand";
    public static final String EVENT_ACTION_CLICK_GROUP_CHAT = "click on group chat";

    private static final String EVENT_NAME_CLICK_GROUPCHAT = "clickGroupChat";
    private static final String EVENT_NAME_CLICK_SHARE = "clickShare";
    public static final String EVENT_NAME_CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer";

    public static final String COMPONENT_FLASH_SALE = "flashsale";
    public static final String COMPONENT_BANNER = "banner";
    public static final String COMPONENT_VOTE = "vote";
    public static final String COMPONENT_PARTNER = "partner";


    private static final String ATTRIBUTE_GROUP_CHAT = "Group Chat";
    public static final String ATTRIBUTE_FLASH_SALE = "Flash Sale";
    public static final String ATTRIBUTE_BANNER = "Banner";
    public static final String ATTRIBUTE_PARTNER_LOGO = "Logo";



    @Inject
    public StreamAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickGroupChatList(String id) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_LIST,
                EVENT_ACTION_GROUPCHAT_LIST,
                id
        );
    }

    public void eventClickVote(String type, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_VOTE,
                type + " - " + channelName
        );
    }

    public void eventClickShare() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_SHARE,
                ""
        );
    }

    public void eventClickShareChannel(String channelType, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SHARE,
                EVENT_CATEGORY_SHARE,
                EVENT_ACTION_SHARE_CHANNEL,
                channelType + " - " + channelName
        );
    }

    public void eventClickJoin() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_JOIN_VOTE_NOW,
                ""
        );
    }

    public void eventClickThumbnail(String id) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_THUMBNAIL,
                id
        );
    }

    public void eventClickComponent(String componentType, String componentName, String
            attributeName, String channelUrl, String channelName) {
        HashMap<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(EVENT_NAME, EVENT_NAME_CLICK_GROUPCHAT);
        eventTracking.put(EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM);
        eventTracking.put(EVENT_ACTION, EVENT_ACTION_CLICK_VOTE_COMPONENT + componentType);
        eventTracking.put(EVENT_LABEL, componentType + " " + componentName);
        eventTracking.put(TRACKER_ATTRIBUTION, generateTrackerAttribution(attributeName,
                channelUrl, channelName));
        analyticTracker.sendEventTracking(eventTracking);
    }

    public static String generateTrackerAttribution(String attributeName, String channelUrl, String
            channelName) {
        return String.format("%s - " + ATTRIBUTE_GROUP_CHAT + " -" +
                " %s - %s", attributeName, channelUrl, channelName);
    }

    public void eventClickVoteComponent(String componentType, String componentName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_VOTE_COMPONENT + componentType,
                componentType+" "+componentName
        );
    }
}
