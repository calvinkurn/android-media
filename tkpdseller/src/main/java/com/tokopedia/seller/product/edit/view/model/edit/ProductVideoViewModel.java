
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVideoViewModel {

    //currently only from youtube
    public static final String YOUTUBE_SOURCE = "youtube";

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("source")
    @Expose
    private String source;

    public ProductVideoViewModel(String url) {
        this.url = url;
        this.source = YOUTUBE_SOURCE;
    }

    public ProductVideoViewModel(String url, String source) {
        this.url = url;
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
