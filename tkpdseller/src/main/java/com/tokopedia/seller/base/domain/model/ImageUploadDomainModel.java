package com.tokopedia.seller.base.domain.model;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class ImageUploadDomainModel<T> {
    private T dataResultImageUpload;
    private String serverId;
    private String url;

    private final Class<T> type;

    public ImageUploadDomainModel(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public T getDataResultImageUpload() {
        return dataResultImageUpload;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDataResultImageUpload(T dataResultImageUpload) {
        this.dataResultImageUpload = dataResultImageUpload;
    }
}
