
package com.tokopedia.tkpdstream.chatroom.domain.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistic {

    @SerializedName("total_voter")
    @Expose
    private int totalVoter;
    @SerializedName("statistic_options")
    @Expose
    private List<StatisticOption> statisticOptions = null;

    public int getTotalVoter() {
        return totalVoter;
    }

    public void setTotalVoter(int totalVoter) {
        this.totalVoter = totalVoter;
    }

    public List<StatisticOption> getStatisticOptions() {
        return statisticOptions;
    }

    public void setStatisticOptions(List<StatisticOption> statisticOptions) {
        this.statisticOptions = statisticOptions;
    }

}
