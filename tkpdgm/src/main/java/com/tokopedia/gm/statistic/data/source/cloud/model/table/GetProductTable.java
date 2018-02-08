package com.tokopedia.gm.statistic.data.source.cloud.model.table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class GetProductTable {

    @SerializedName("cells")
    @Expose
    private List<Cell> cells;
    @SerializedName("total_cell_count")
    @Expose
    private long totalCellCount;

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public long getTotalCellCount() {
        return totalCellCount;
    }

    public void setTotalCellCount(long totalCellCount) {
        this.totalCellCount = totalCellCount;
    }

    public static class Cell {

        @SerializedName("product.product_id")
        @Expose
        private long productProductId;
        @SerializedName("product.product_name")
        @Expose
        private String productProductName;
        @SerializedName("product.product_link")
        @Expose
        private String productProductLink;
        @SerializedName("sold_sum")
        @Expose
        private long soldSum;
        @SerializedName("view_sum")
        @Expose
        private long viewSum;
        @SerializedName("trans_sum")
        @Expose
        private long transSum;

        public long getProductProductId() {
            return productProductId;
        }

        public void setProductProductId(long productProductId) {
            this.productProductId = productProductId;
        }

        public String getProductProductName() {
            return productProductName;
        }

        public void setProductProductName(String productProductName) {
            this.productProductName = productProductName;
        }

        public String getProductProductLink() {
            return productProductLink;
        }

        public void setProductProductLink(String productProductLink) {
            this.productProductLink = productProductLink;
        }

        public long getSoldSum() {
            return soldSum;
        }

        public void setSoldSum(long soldSum) {
            this.soldSum = soldSum;
        }

        public long getViewSum() {
            return viewSum;
        }

        public void setViewSum(long viewSum) {
            this.viewSum = viewSum;
        }

        public long getTransSum() {
            return transSum;
        }

        public void setTransSum(long transSum) {
            this.transSum = transSum;
        }

    }
}
