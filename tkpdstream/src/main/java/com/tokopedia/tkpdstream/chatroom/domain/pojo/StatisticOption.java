
package com.tokopedia.tkpdstream.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticOption {

    @SerializedName("option_id")
    @Expose
    private int optionId;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("voter")
    @Expose
    private int voter;
    @SerializedName("percentage")
    @Expose
    private int percentage;
    @SerializedName("is_selected")
    @Expose
    private boolean isSelected;

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

    public int getVoter() {
        return voter;
    }

    public void setVoter(int voter) {
        this.voter = voter;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
