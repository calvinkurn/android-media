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

    private Section section;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public static class Section {

        private long sectionId;
        private String name;
        private String iconUrl;

        public long getSectionId() {
            return sectionId;
        }

        public void setSectionId(long sectionId) {
            this.sectionId = sectionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Section section = (Section) o;

            if (sectionId != section.sectionId) return false;
            if (name != null ? !name.equals(section.name) : section.name != null) return false;
            return iconUrl != null ? iconUrl.equals(section.iconUrl) : section.iconUrl == null;

        }

        @Override
        public int hashCode() {
            int result = (int) (sectionId ^ (sectionId >>> 32));
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SellerInfoModel that = (SellerInfoModel) o;

        if (infoId != that.infoId) return false;
        if (status != that.status) return false;
        if (isRead != that.isRead) return false;
        if (createTimeUnix != that.createTimeUnix) return false;
        if (expireTimeUnix != that.expireTimeUnix) return false;
        if (isToday != that.isToday) return false;
        if (isYesterday != that.isYesterday) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (shortDescription != null ? !shortDescription.equals(that.shortDescription) : that.shortDescription != null)
            return false;
        if (externalLink != null ? !externalLink.equals(that.externalLink) : that.externalLink != null)
            return false;
        if (infoThumbnailUrl != null ? !infoThumbnailUrl.equals(that.infoThumbnailUrl) : that.infoThumbnailUrl != null)
            return false;
        return section != null ? section.equals(that.section) : that.section == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (infoId ^ (infoId >>> 32));
        result = 31 * result + (int) (status ^ (status >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        result = 31 * result + (externalLink != null ? externalLink.hashCode() : 0);
        result = 31 * result + (infoThumbnailUrl != null ? infoThumbnailUrl.hashCode() : 0);
        result = 31 * result + (isRead ? 1 : 0);
        result = 31 * result + (int) (createTimeUnix ^ (createTimeUnix >>> 32));
        result = 31 * result + (int) (expireTimeUnix ^ (expireTimeUnix >>> 32));
        result = 31 * result + (isToday ? 1 : 0);
        result = 31 * result + (isYesterday ? 1 : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        return result;
    }
}
