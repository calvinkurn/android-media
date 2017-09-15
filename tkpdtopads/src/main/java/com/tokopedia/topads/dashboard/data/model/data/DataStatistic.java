package com.tokopedia.topads.dashboard.data.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataStatistic {

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("cells")
    @Expose
    private List<Cell> cells = new ArrayList<Cell>();

    /**
     *
     * @return
     * The summary
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     * The summary
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     * The cells
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     *
     * @param cells
     * The cells
     */
    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

}