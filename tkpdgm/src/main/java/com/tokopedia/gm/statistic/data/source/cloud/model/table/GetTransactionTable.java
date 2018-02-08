package com.tokopedia.gm.statistic.data.source.cloud.model.table;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 7/6/17.
 */
public class GetTransactionTable {

    @SerializedName("cells")
    @Expose
    private List<Cell> cells = null;
    @SerializedName("total_cell_count")
    @Expose
    private int totalCellCount;

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public int getTotalCellCount() {
        return totalCellCount;
    }

    public void setTotalCellCount(int totalCellCount) {
        this.totalCellCount = totalCellCount;
    }

    public static class Cell {

        @SerializedName("product.product_id")
        @Expose
        private int productProductId;
        @SerializedName("product.product_name")
        @Expose
        private String productProductName;
        @SerializedName("product.product_link")
        @Expose
        private String productProductLink;
        @SerializedName("trans_sum")
        @Expose
        private int transSum;
        @SerializedName("order_sum")
        @Expose
        private int orderSum;
        @SerializedName("delivered_sum")
        @Expose
        private int deliveredSum;
        @SerializedName("rejected_sum")
        @Expose
        private int rejectedSum;
        @SerializedName("delivered_amt")
        @Expose
        private int deliveredAmt;
        @SerializedName("rejected_amt")
        @Expose
        private int rejectedAmt;

        public int getProductProductId() {
            return productProductId;
        }

        public void setProductProductId(int productProductId) {
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

        public int getTransSum() {
            return transSum;
        }

        public void setTransSum(int transSum) {
            this.transSum = transSum;
        }

        public int getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(int orderSum) {
            this.orderSum = orderSum;
        }

        public int getDeliveredSum() {
            return deliveredSum;
        }

        public void setDeliveredSum(int deliveredSum) {
            this.deliveredSum = deliveredSum;
        }

        public int getRejectedSum() {
            return rejectedSum;
        }

        public void setRejectedSum(int rejectedSum) {
            this.rejectedSum = rejectedSum;
        }

        public int getDeliveredAmt() {
            return deliveredAmt;
        }

        public void setDeliveredAmt(int deliveredAmt) {
            this.deliveredAmt = deliveredAmt;
        }

        public int getRejectedAmt() {
            return rejectedAmt;
        }

        public void setRejectedAmt(int rejectedAmt) {
            this.rejectedAmt = rejectedAmt;
        }

    }
}
