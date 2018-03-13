
package com.tokopedia.tkpdstream.channel.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("option_id")
    @Expose
    private int optionId;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("image_option")
    @Expose
    private String imageOption;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getImageOption() {
        return imageOption;
    }

    public void setImageOption(String imageOption) {
        this.imageOption = imageOption;
    }

}
