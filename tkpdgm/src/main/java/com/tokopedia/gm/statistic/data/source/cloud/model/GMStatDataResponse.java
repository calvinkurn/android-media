package com.tokopedia.gm.statistic.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class GMStatDataResponse<T> {
    @SerializedName("cells")
    @Expose
    private List<T> cells = null;
    @SerializedName("total_cell_count")
    @Expose
    private long totalCellCount;

    public List<T> getCells() {
        return cells;
    }

    public void setCells(List<T> cells) {
        this.cells = cells;
    }

    public long getTotalCellCount() {
        return totalCellCount;
    }

    public void setTotalCellCount(long totalCellCount) {
        this.totalCellCount = totalCellCount;
    }
}
