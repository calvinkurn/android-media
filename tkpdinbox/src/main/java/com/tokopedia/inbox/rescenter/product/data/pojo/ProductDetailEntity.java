package com.tokopedia.inbox.rescenter.product.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/29/17.
 */

public class ProductDetailEntity {


    @SerializedName("product")
    private Product product;
    @SerializedName("trouble")
    private Trouble trouble;
    @SerializedName("remark")
    private String remark;
    @SerializedName("attachments")
    private List<Attachments> attachments;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Trouble getTrouble() {
        return trouble;
    }

    public void setTrouble(Trouble trouble) {
        this.trouble = trouble;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }

    public static class Product {
        @SerializedName("photo")
        private Photo photo;
        @SerializedName("name")
        private String name;
        @SerializedName("price")
        private int price;

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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
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

    public static class Trouble {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Attachments {
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
