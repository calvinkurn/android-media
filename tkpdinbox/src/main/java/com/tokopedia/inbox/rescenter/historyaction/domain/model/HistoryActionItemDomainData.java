package com.tokopedia.inbox.rescenter.historyaction.domain.model;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryActionItemDomainData {
    private int actionBy;
    private String actionByText;
    private String conversationID;
    private String date;
    private String historyStr;

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionByText() {
        return actionByText;
    }

    public void setActionByText(String actionByText) {
        this.actionByText = actionByText;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHistoryStr(String historyStr) {
        this.historyStr = historyStr;
    }

    public String getHistoryStr() {
        return historyStr;
    }
}
