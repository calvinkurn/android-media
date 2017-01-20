package com.tokopedia.seller.gmstat.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionGraph {

    @SerializedName("FinishedTrans")
    @Expose
    private long finishedTrans;
    @SerializedName("DiffFinishedTrans")
    @Expose
    private double diffFinishedTrans;
    @SerializedName("SuccessTrans")
    @Expose
    private long successTrans;
    @SerializedName("NewOrder")
    @Expose
    private int newOrder;
    @SerializedName("DeliveredProduct")
    @Expose
    private int deliveredProduct;
    @SerializedName("GrossRevenue")
    @Expose
    private long grossRevenue;
    @SerializedName("NetRevenue")
    @Expose
    private int netRevenue;
    @SerializedName("RejectedAmount")
    @Expose
    private int rejectedAmount;
    @SerializedName("ShippingCost")
    @Expose
    private int shippingCost;
    @SerializedName("CpcProduct")
    @Expose
    private int cpcProduct;
    @SerializedName("CpcShop")
    @Expose
    private int cpcShop;
    @SerializedName("DiffSuccessTrans")
    @Expose
    private double diffSuccessTrans;
    @SerializedName("DiffNewOrder")
    @Expose
    private double diffNewOrder;
    @SerializedName("DiffDeliveredProduct")
    @Expose
    private double diffDeliveredProduct;
    @SerializedName("DiffGrossRevenue")
    @Expose
    private double diffGrossRevenue;
    @SerializedName("DiffNetRevenue")
    @Expose
    private double diffNetRevenue;
    @SerializedName("DiffRejectedAmount")
    @Expose
    private double diffRejectedAmount;
    @SerializedName("DiffShippingCost")
    @Expose
    private double diffShippingCost;
    @SerializedName("DiffCpcShop")
    @Expose
    private double diffCpcShop;
    @SerializedName("DiffCpcProd")
    @Expose
    private double diffCpcProd;
    @SerializedName("DateGraph")
    @Expose
    private List<Integer> dateGraph = null;
    @SerializedName("SuccessTransGraph")
    @Expose
    private List<Integer> successTransGraph = null;
    @SerializedName("RejectedSumGraph")
    @Expose
    private List<Integer> rejectedSumGraph = null;
    @SerializedName("GrossGraph")
    @Expose
    private List<Integer> grossGraph = null;
    @SerializedName("NetGraph")
    @Expose
    private List<Integer> netGraph = null;
    @SerializedName("RejectedAmtGraph")
    @Expose
    private List<Integer> rejectedAmtGraph = null;
    @SerializedName("ShippingGraph")
    @Expose
    private List<Integer> shippingGraph = null;
    @SerializedName("AdsPGraph")
    @Expose
    private List<Integer> adsPGraph = null;
    @SerializedName("AdsSGraph")
    @Expose
    private List<Integer> adsSGraph = null;
    @SerializedName("PDateGraph")
    @Expose
    private List<Integer> pDateGraph = null;
    @SerializedName("PSuccessTransGraph")
    @Expose
    private List<Integer> pSuccessTransGraph = null;
    @SerializedName("PRejectedSumGraph")
    @Expose
    private List<Integer> pRejectedSumGraph = null;
    @SerializedName("PGrossGraph")
    @Expose
    private List<Integer> pGrossGraph = null;
    @SerializedName("PNetGraph")
    @Expose
    private List<Integer> pNetGraph = null;
    @SerializedName("PRejectedAmtGraph")
    @Expose
    private List<Integer> pRejectedAmtGraph = null;
    @SerializedName("PShippingGraph")
    @Expose
    private List<Integer> pShippingGraph = null;
    @SerializedName("PAdsPGraph")
    @Expose
    private List<Integer> pAdsPGraph = null;
    @SerializedName("PAdsSGraph")
    @Expose
    private List<Integer> pAdsSGraph = null;
    @SerializedName("RejectedTransGraph")
    @Expose
    private List<Integer> rejectedTransGraph = null;

    public List<Integer> getRejectedTransGraph() {
        return rejectedTransGraph;
    }

    public void setRejectedTransGraph(List<Integer> rejectedTransGraph) {
        this.rejectedTransGraph = rejectedTransGraph;
    }

    public long getFinishedTrans() {
        return finishedTrans;
    }

    public void setFinishedTrans(long finishedTrans) {
        this.finishedTrans = finishedTrans;
    }

    public double getDiffFinishedTrans() {
        return diffFinishedTrans;
    }

    public void setDiffFinishedTrans(double diffFinishedTrans) {
        this.diffFinishedTrans = diffFinishedTrans;
    }

    public long getSuccessTrans() {
        return successTrans;
    }

    public void setSuccessTrans(long successTrans) {
        this.successTrans = successTrans;
    }

    public int getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(int newOrder) {
        this.newOrder = newOrder;
    }

    public int getDeliveredProduct() {
        return deliveredProduct;
    }

    public void setDeliveredProduct(int deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    public long getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(long grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public int getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(int netRevenue) {
        this.netRevenue = netRevenue;
    }

    public int getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(int rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getCpcProduct() {
        return cpcProduct;
    }

    public void setCpcProduct(int cpcProduct) {
        this.cpcProduct = cpcProduct;
    }

    public int getCpcShop() {
        return cpcShop;
    }

    public void setCpcShop(int cpcShop) {
        this.cpcShop = cpcShop;
    }

    public double getDiffSuccessTrans() {
        return diffSuccessTrans;
    }

    public void setDiffSuccessTrans(double diffSuccessTrans) {
        this.diffSuccessTrans = diffSuccessTrans;
    }

    public double getDiffNewOrder() {
        return diffNewOrder;
    }

    public void setDiffNewOrder(double diffNewOrder) {
        this.diffNewOrder = diffNewOrder;
    }

    public double getDiffDeliveredProduct() {
        return diffDeliveredProduct;
    }

    public void setDiffDeliveredProduct(double diffDeliveredProduct) {
        this.diffDeliveredProduct = diffDeliveredProduct;
    }

    public double getDiffGrossRevenue() {
        return diffGrossRevenue;
    }

    public void setDiffGrossRevenue(double diffGrossRevenue) {
        this.diffGrossRevenue = diffGrossRevenue;
    }

    public double getDiffNetRevenue() {
        return diffNetRevenue;
    }

    public void setDiffNetRevenue(double diffNetRevenue) {
        this.diffNetRevenue = diffNetRevenue;
    }

    public double getDiffRejectedAmount() {
        return diffRejectedAmount;
    }

    public void setDiffRejectedAmount(double diffRejectedAmount) {
        this.diffRejectedAmount = diffRejectedAmount;
    }

    public double getDiffShippingCost() {
        return diffShippingCost;
    }

    public void setDiffShippingCost(double diffShippingCost) {
        this.diffShippingCost = diffShippingCost;
    }

    public double getDiffCpcShop() {
        return diffCpcShop;
    }

    public void setDiffCpcShop(double diffCpcShop) {
        this.diffCpcShop = diffCpcShop;
    }

    public double getDiffCpcProd() {
        return diffCpcProd;
    }

    public void setDiffCpcProd(double diffCpcProd) {
        this.diffCpcProd = diffCpcProd;
    }

    public List<Integer> getDateGraph() {
        return dateGraph;
    }

    public void setDateGraph(List<Integer> dateGraph) {
        this.dateGraph = dateGraph;
    }

    public List<Integer> getSuccessTransGraph() {
        return successTransGraph;
    }

    public void setSuccessTransGraph(List<Integer> successTransGraph) {
        this.successTransGraph = successTransGraph;
    }

    public List<Integer> getRejectedSumGraph() {
        return rejectedSumGraph;
    }

    public void setRejectedSumGraph(List<Integer> rejectedSumGraph) {
        this.rejectedSumGraph = rejectedSumGraph;
    }

    public List<Integer> getGrossGraph() {
        return grossGraph;
    }

    public void setGrossGraph(List<Integer> grossGraph) {
        this.grossGraph = grossGraph;
    }

    public List<Integer> getNetGraph() {
        return netGraph;
    }

    public void setNetGraph(List<Integer> netGraph) {
        this.netGraph = netGraph;
    }

    public List<Integer> getRejectedAmtGraph() {
        return rejectedAmtGraph;
    }

    public void setRejectedAmtGraph(List<Integer> rejectedAmtGraph) {
        this.rejectedAmtGraph = rejectedAmtGraph;
    }

    public List<Integer> getShippingGraph() {
        return shippingGraph;
    }

    public void setShippingGraph(List<Integer> shippingGraph) {
        this.shippingGraph = shippingGraph;
    }

    public List<Integer> getAdsPGraph() {
        return adsPGraph;
    }

    public void setAdsPGraph(List<Integer> adsPGraph) {
        this.adsPGraph = adsPGraph;
    }

    public List<Integer> getAdsSGraph() {
        return adsSGraph;
    }

    public void setAdsSGraph(List<Integer> adsSGraph) {
        this.adsSGraph = adsSGraph;
    }

    public List<Integer> getPDateGraph() {
        return pDateGraph;
    }

    public void setPDateGraph(List<Integer> pDateGraph) {
        this.pDateGraph = pDateGraph;
    }

    public List<Integer> getPSuccessTransGraph() {
        return pSuccessTransGraph;
    }

    public void setPSuccessTransGraph(List<Integer> pSuccessTransGraph) {
        this.pSuccessTransGraph = pSuccessTransGraph;
    }

    public List<Integer> getPRejectedSumGraph() {
        return pRejectedSumGraph;
    }

    public void setPRejectedSumGraph(List<Integer> pRejectedSumGraph) {
        this.pRejectedSumGraph = pRejectedSumGraph;
    }

    public List<Integer> getPGrossGraph() {
        return pGrossGraph;
    }

    public void setPGrossGraph(List<Integer> pGrossGraph) {
        this.pGrossGraph = pGrossGraph;
    }

    public List<Integer> getPNetGraph() {
        return pNetGraph;
    }

    public void setPNetGraph(List<Integer> pNetGraph) {
        this.pNetGraph = pNetGraph;
    }

    public List<Integer> getPRejectedAmtGraph() {
        return pRejectedAmtGraph;
    }

    public void setPRejectedAmtGraph(List<Integer> pRejectedAmtGraph) {
        this.pRejectedAmtGraph = pRejectedAmtGraph;
    }

    public List<Integer> getPShippingGraph() {
        return pShippingGraph;
    }

    public void setPShippingGraph(List<Integer> pShippingGraph) {
        this.pShippingGraph = pShippingGraph;
    }

    public List<Integer> getPAdsPGraph() {
        return pAdsPGraph;
    }

    public void setPAdsPGraph(List<Integer> pAdsPGraph) {
        this.pAdsPGraph = pAdsPGraph;
    }

    public List<Integer> getPAdsSGraph() {
        return pAdsSGraph;
    }

    public void setPAdsSGraph(List<Integer> pAdsSGraph) {
        this.pAdsSGraph = pAdsSGraph;
    }

    @Override
    public String toString() {
        return "GetTransactionGraph{" +
                "successTrans=" + successTrans +
                ", newOrder=" + newOrder +
                ", deliveredProduct=" + deliveredProduct +
                ", grossRevenue=" + grossRevenue +
                ", netRevenue=" + netRevenue +
                ", rejectedAmount=" + rejectedAmount +
                ", shippingCost=" + shippingCost +
                ", cpcProduct=" + cpcProduct +
                ", cpcShop=" + cpcShop +
                ", diffSuccessTrans=" + diffSuccessTrans +
                ", diffNewOrder=" + diffNewOrder +
                ", diffDeliveredProduct=" + diffDeliveredProduct +
                ", diffGrossRevenue=" + diffGrossRevenue +
                ", diffNetRevenue=" + diffNetRevenue +
                ", diffRejectedAmount=" + diffRejectedAmount +
                ", diffShippingCost=" + diffShippingCost +
                ", diffCpcShop=" + diffCpcShop +
                ", diffCpcProd=" + diffCpcProd +
                ", dateGraph=" + dateGraph +
                ", successTransGraph=" + successTransGraph +
                ", rejectedSumGraph=" + rejectedSumGraph +
                ", grossGraph=" + grossGraph +
                ", netGraph=" + netGraph +
                ", rejectedAmtGraph=" + rejectedAmtGraph +
                ", shippingGraph=" + shippingGraph +
                ", adsPGraph=" + adsPGraph +
                ", adsSGraph=" + adsSGraph +
                ", pDateGraph=" + pDateGraph +
                ", pSuccessTransGraph=" + pSuccessTransGraph +
                ", pRejectedSumGraph=" + pRejectedSumGraph +
                ", pGrossGraph=" + pGrossGraph +
                ", pNetGraph=" + pNetGraph +
                ", pRejectedAmtGraph=" + pRejectedAmtGraph +
                ", pShippingGraph=" + pShippingGraph +
                ", pAdsPGraph=" + pAdsPGraph +
                ", pAdsSGraph=" + pAdsSGraph +
                '}';
    }
}