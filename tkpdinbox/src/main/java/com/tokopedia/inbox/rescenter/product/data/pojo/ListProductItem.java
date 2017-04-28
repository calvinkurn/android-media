package com.tokopedia.inbox.rescenter.product.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListProductItem {
    @SerializedName("resProductId")
    private String resProductId;
    @SerializedName("photo")
    private Photo photo;
    @SerializedName("name")
    private String name;

    public String getResProductId() {
        return resProductId;
    }

    public void setResProductId(String resProductId) {
        this.resProductId = resProductId;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Photo {
        @SerializedName("imageThumb")
        private String imageThumb;
        @SerializedName("url")
        private String url;

        public String getImageThumb() {
            return imageThumb;
        }

        public void setImageThumb(String imageThumb) {
            this.imageThumb = imageThumb;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
