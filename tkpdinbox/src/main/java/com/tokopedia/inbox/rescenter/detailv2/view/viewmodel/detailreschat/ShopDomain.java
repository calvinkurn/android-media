package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ShopDomain {

    private int id;
    private String name;
    private String url;
    private String imageThumb;
    private String image;

    public ShopDomain(int id, String name, String url, String imageThumb, String image) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.imageThumb = imageThumb;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
