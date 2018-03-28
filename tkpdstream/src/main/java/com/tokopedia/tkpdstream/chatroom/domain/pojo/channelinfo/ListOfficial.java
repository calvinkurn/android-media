
package com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListOfficial {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("list_brands")
    @Expose
    private List<ListBrand> listBrands = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListBrand> getListBrands() {
        return listBrands;
    }

    public void setListBrands(List<ListBrand> listBrands) {
        this.listBrands = listBrands;
    }

}
