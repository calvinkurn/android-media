package com.tokopedia.seller.seller.info.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoModel implements ItemType {
    public static final int TYPE = 1921912;

    private long infoId;
    private long status;
    private String title;
    private String content;
    private String shortDescription;
    private String externalLink;
    private String infoThumbnailUrl;
    private boolean isRead;
    private long createTimeUnix;
    private long expireTimeUnix;

    private boolean isToday, isYesterday;

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isYesterday() {
        return isYesterday;
    }

    public void setYesterday(boolean yesterday) {
        isYesterday = yesterday;
    }

    public static int getTYPE() {
        return TYPE;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getInfoThumbnailUrl() {
        return infoThumbnailUrl;
    }

    public void setInfoThumbnailUrl(String infoThumbnailUrl) {
        this.infoThumbnailUrl = infoThumbnailUrl;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getCreateTimeUnix() {
        return createTimeUnix;
    }

    public void setCreateTimeUnix(long createTimeUnix) {
        this.createTimeUnix = createTimeUnix;
    }

    public long getExpireTimeUnix() {
        return expireTimeUnix;
    }

    public void setExpireTimeUnix(long expireTimeUnix) {
        this.expireTimeUnix = expireTimeUnix;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
