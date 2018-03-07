package com.tokopedia.tkpdstream.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author by StevenFredian on 05/03/18.
 */

public class StreamAnalytics {
    private AnalyticTracker analyticTracker;

    private String EVENT_CATEGORY_GROUPCHAT_LIST = "groupchat";
    private String EVENT_CATEGORY_GROUPCHAT_ROOM = "groupchat room";
    private String EVENT_CATEGORY_SHARE = "share page";
    private String EVENT_CATEGORY_INBOX_CHAT = "inbox-chat";

    private String EVENT_ACTION_GROUPCHAT_LIST = "click on group chat list";
    private String EVENT_ACTION_VOTE = "click on vote";
    private String EVENT_ACTION_SHARE = "click on share";
    private String EVENT_ACTION_SHARE_CHANNEL = "click share channel";
    private String EVENT_ACTION_JOIN_VOTE_NOW = "click on join";
    private String EVENT_ACTION_CLICK_THUMBNAIL= "click on image thumbnail";
    private String EVENT_ACTION_CLICK_VOTE_COMPONENT= "click on component - ";
    private String EVENT_ACTION_CLICK_VOTE_EXPAND= "click on vote expand";
    private String EVENT_ACTION_CLICK_COMMUNITY_TAB= "click on community tab";

    private String EVENT_NAME_CLICK_GROUPCHAT = "clickGroupChat";
    private String EVENT_NAME_CLICK_SHARE = "clickShare";
    private String EVENT_NAME_CLICK_INBOXCHAT = "clickInboxChat";

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
                type+" - "+channelName
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
                channelType+" - "+channelName
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

    public void eventClickVoteComponent(String componentType, String componentName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_VOTE_COMPONENT + componentType,
                componentType+" "+componentName;
        );
    }

    public void eventClickVoteExpand() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_VOTE_EXPAND,
                ""
        );
    }

    public void eventClickInboxChat() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_INBOXCHAT,
                EVENT_CATEGORY_INBOX_CHAT,
                EVENT_ACTION_CLICK_COMMUNITY_TAB,
                ""
        );
    }

}
