
package com.tokopedia.tokocash.qrpayment.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class BalanceTokoCashEntity {

    @SerializedName("text")
    @Expose
    private String titleText;
    @SerializedName("action")
    @Expose
    private ActionBalanceEntity actionBalanceEntity;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("raw_balance")
    @Expose
    private int raw_balance;
    @SerializedName("total_balance")
    @Expose
    private String totalBalance;
    @SerializedName("raw_total_balance")
    @Expose
    private long rawTotalBalance;
    @SerializedName("hold_balance")
    @Expose
    private String holdBalance;
    @SerializedName("raw_hold_balance")
    @Expose
    private long rawHoldBalance;
    @SerializedName("applinks")
    @Expose
    private String applinks;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("link")
    @Expose
    private int link;
    @SerializedName("raw_threshold")
    @Expose
    private long rawThreshold;
    @SerializedName("threshold")
    @Expose
    private String threshold;
    @SerializedName("ab_tags")
    @Expose
    private List<String> abTags;

    public BalanceTokoCashEntity() {
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public ActionBalanceEntity getActionBalanceEntity() {
        return actionBalanceEntity;
    }

    public void setActionBalanceEntity(ActionBalanceEntity actionBalanceEntity) {
        this.actionBalanceEntity = actionBalanceEntity;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getRaw_balance() {
        return raw_balance;
    }

    public void setRaw_balance(int raw_balance) {
        this.raw_balance = raw_balance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getRawTotalBalance() {
        return rawTotalBalance;
    }

    public void setRawTotalBalance(long rawTotalBalance) {
        this.rawTotalBalance = rawTotalBalance;
    }

    public String getHoldBalance() {
        return holdBalance;
    }

    public void setHoldBalance(String holdBalance) {
        this.holdBalance = holdBalance;
    }

    public long getRawHoldBalance() {
        return rawHoldBalance;
    }

    public void setRawHoldBalance(long rawHoldBalance) {
        this.rawHoldBalance = rawHoldBalance;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public long getRawThreshold() {
        return rawThreshold;
    }

    public void setRawThreshold(long rawThreshold) {
        this.rawThreshold = rawThreshold;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }
}