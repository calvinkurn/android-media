package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/13/17.
 */

public class HistoryItem {
    private String provider;
    private String date;
    private String historyText;
    private boolean latest;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHistoryText() {
        return historyText;
    }

    public void setHistoryText(String historyText) {
        this.historyText = historyText;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

}
