
package com.tokopedia.tkpdstream.vote.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticOption {

    @SerializedName("option_id")
    @Expose
    private String optionId;

    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("voter")
    @Expose
    private int voter;
    @SerializedName("percentage")
    @Expose
    private double percentage;

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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getOptionId() {
        return optionId;
    }
}
