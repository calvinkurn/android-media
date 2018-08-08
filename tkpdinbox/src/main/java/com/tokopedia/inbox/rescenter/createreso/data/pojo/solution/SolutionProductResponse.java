package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProductResponse {

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("image")
    private SolutionProductImageResponse image;

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

    public SolutionProductImageResponse getImage() {
        return image;
    }

    public void setImage(SolutionProductImageResponse image) {
        this.image = image;
    }

}
