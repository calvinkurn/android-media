package com.tokopedia.gm.statistic.data.source.cloud.model.table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class GetBuyerTable {
    @SerializedName("cells")
    @Expose
    private List<Cell> cells = null;
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

        @SerializedName("buyer.customer_id")
        @Expose
        private long buyerCustomerId;
        @SerializedName("buyer.sex")
        @Expose
        private long buyerSex;
        @SerializedName("buyer.customer_name")
        @Expose
        private String buyerCustomerName;
        @SerializedName("trans_cnt")
        @Expose
        private long transCnt;
        @SerializedName("product_cnt")
        @Expose
        private long productCnt;
        @SerializedName("trans_amt")
        @Expose
        private long transAmt;

        public long getBuyerCustomerId() {
            return buyerCustomerId;
        }

        public void setBuyerCustomerId(long buyerCustomerId) {
            this.buyerCustomerId = buyerCustomerId;
        }

        public long getBuyerSex() {
            return buyerSex;
        }

        public void setBuyerSex(long buyerSex) {
            this.buyerSex = buyerSex;
        }

        public String getBuyerCustomerName() {
            return buyerCustomerName;
        }

        public void setBuyerCustomerName(String buyerCustomerName) {
            this.buyerCustomerName = buyerCustomerName;
        }

        public long getTransCnt() {
            return transCnt;
        }

        public void setTransCnt(long transCnt) {
            this.transCnt = transCnt;
        }

        public long getProductCnt() {
            return productCnt;
        }

        public void setProductCnt(long productCnt) {
            this.productCnt = productCnt;
        }

        public long getTransAmt() {
            return transAmt;
        }

        public void setTransAmt(long transAmt) {
            this.transAmt = transAmt;
        }

    }
}
