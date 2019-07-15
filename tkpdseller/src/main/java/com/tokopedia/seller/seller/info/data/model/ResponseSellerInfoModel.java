package com.tokopedia.seller.seller.info.data.model;

/**
 * Created by normansyahputa on 11/30/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSellerInfoModel {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private double serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server")
    @Expose
    private String server;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(double serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static class Data {

        @SerializedName("paging")
        @Expose
        private Paging paging;
        @SerializedName("list")
        @Expose
        private java.util.List<List> list = null;

        public Paging getPaging() {
            return paging;
        }

        public void setPaging(Paging paging) {
            this.paging = paging;
        }

        public java.util.List<List> getList() {
            return list;
        }

        public void setList(java.util.List<List> list) {
            this.list = list;
        }

    }

    public static class Links {

        @SerializedName("self")
        @Expose
        private String self;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

    }

    public static class List {

        @SerializedName("info_id")
        @Expose
        private long infoId;
        @SerializedName("status")
        @Expose
        private long status;
        @SerializedName("type")
        @Expose
        private long type;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("short_description")
        @Expose
        private String shortDescription;
        @SerializedName("external_link")
        @Expose
        private String externalLink;
        @SerializedName("info_thumbnail_url")
        @Expose
        private String infoThumbnailUrl;
        @SerializedName("is_read")
        @Expose
        private boolean isRead;
        @SerializedName("create_time_unix")
        @Expose
        private long createTimeUnix;
        @SerializedName("expire_time_unix")
        @Expose
        private long expireTimeUnix;
        @SerializedName("section")
        @Expose
        private Section section;

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

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
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

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
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

        public Section getSection() {
            return section;
        }

        public void setSection(Section section) {
            this.section = section;
        }

    }

    public static class Paging {

        @SerializedName("has_next")
        @Expose
        private boolean hasNext;
        @SerializedName("has_prev")
        @Expose
        private boolean hasPrev;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPrev() {
            return hasPrev;
        }

        public void setHasPrev(boolean hasPrev) {
            this.hasPrev = hasPrev;
        }

    }

    public static class Section {

        @SerializedName("section_id")
        @Expose
        private long sectionId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("icon_url")
        @Expose
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

    }
}