package com.tokopedia.seller.gmstat.models;

/**
 * Created by normansyahputa on 11/2/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetProductGraph {

    @SerializedName("SuccessTrans")
    @Expose
    private long successTrans;
    @SerializedName("ProductView")
    @Expose
    private long productView;
    @SerializedName("ProductSold")
    @Expose
    private long productSold;
    @SerializedName("ConversionRate")
    @Expose
    private double conversionRate;
    @SerializedName("DiffTrans")
    @Expose
    private double diffTrans;
    @SerializedName("DiffConv")
    @Expose
    private double diffConv;
    @SerializedName("DiffView")
    @Expose
    private double diffView;
    @SerializedName("DiffSold")
    @Expose
    private double diffSold;
    @SerializedName("DateGraph")
    @Expose
    private List<Integer> dateGraph = new ArrayList<Integer>();
    @SerializedName("ViewGraoh")
    @Expose
    private List<Integer> viewGraoh = new ArrayList<Integer>();
    @SerializedName("ProductSoldGraph")
    @Expose
    private List<Integer> productSoldGraph = new ArrayList<Integer>();
    @SerializedName("SuccessTransGraph")
    @Expose
    private List<Integer> successTransGraph = new ArrayList<Integer>();
    @SerializedName("PDateGraph")
    @Expose
    private List<Integer> pDateGraph = new ArrayList<Integer>();
    @SerializedName("PViewGraph")
    @Expose
    private List<Integer> pViewGraph = new ArrayList<Integer>();
    @SerializedName("PProductSoldGraph")
    @Expose
    private List<Integer> pProductSoldGraph = new ArrayList<Integer>();
    @SerializedName("PSuccessTransGraph")
    @Expose
    private List<Integer> pSuccessTransGraph = new ArrayList<Integer>();

    /**
     *
     * @return
     * The successTrans
     */
    public long getSuccessTrans() {
        return successTrans;
    }

    /**
     *
     * @param successTrans
     * The SuccessTrans
     */
    public void setSuccessTrans(long successTrans) {
        this.successTrans = successTrans;
    }

    /**
     *
     * @return
     * The productView
     */
    public long getProductView() {
        return productView;
    }

    /**
     *
     * @param productView
     * The ProductView
     */
    public void setProductView(long productView) {
        this.productView = productView;
    }

    /**
     *
     * @return
     * The productSold
     */
    public long getProductSold() {
        return productSold;
    }

    /**
     *
     * @param productSold
     * The ProductSold
     */
    public void setProductSold(long productSold) {
        this.productSold = productSold;
    }

    /**
     *
     * @return
     * The conversionRate
     */
    public double getConversionRate() {
        return conversionRate;
    }

    /**
     *
     * @param conversionRate
     * The ConversionRate
     */
    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     *
     * @return
     * The diffTrans
     */
    public double getDiffTrans() {
        return diffTrans;
    }

    /**
     *
     * @param diffTrans
     * The DiffTrans
     */
    public void setDiffTrans(double diffTrans) {
        this.diffTrans = diffTrans;
    }

    /**
     *
     * @return
     * The diffConv
     */
    public double getDiffConv() {
        return diffConv;
    }

    /**
     *
     * @param diffConv
     * The DiffConv
     */
    public void setDiffConv(double diffConv) {
        this.diffConv = diffConv;
    }

    /**
     *
     * @return
     * The diffView
     */
    public double getDiffView() {
        return diffView;
    }

    /**
     *
     * @param diffView
     * The DiffView
     */
    public void setDiffView(double diffView) {
        this.diffView = diffView;
    }

    /**
     *
     * @return
     * The diffSold
     */
    public double getDiffSold() {
        return diffSold;
    }

    /**
     *
     * @param diffSold
     * The DiffSold
     */
    public void setDiffSold(double diffSold) {
        this.diffSold = diffSold;
    }

    /**
     *
     * @return
     * The dateGraph
     */
    public List<Integer> getDateGraph() {
        return dateGraph;
    }

    /**
     *
     * @param dateGraph
     * The DateGraph
     */
    public void setDateGraph(List<Integer> dateGraph) {
        this.dateGraph = dateGraph;
    }

    /**
     *
     * @return
     * The viewGraoh
     */
    public List<Integer> getViewGraoh() {
        return viewGraoh;
    }

    /**
     *
     * @param viewGraoh
     * The ViewGraoh
     */
    public void setViewGraoh(List<Integer> viewGraoh) {
        this.viewGraoh = viewGraoh;
    }

    /**
     *
     * @return
     * The productSoldGraph
     */
    public List<Integer> getProductSoldGraph() {
        return productSoldGraph;
    }

    /**
     *
     * @param productSoldGraph
     * The ProductSoldGraph
     */
    public void setProductSoldGraph(List<Integer> productSoldGraph) {
        this.productSoldGraph = productSoldGraph;
    }

    /**
     *
     * @return
     * The successTransGraph
     */
    public List<Integer> getSuccessTransGraph() {
        return successTransGraph;
    }

    /**
     *
     * @param successTransGraph
     * The SuccessTransGraph
     */
    public void setSuccessTransGraph(List<Integer> successTransGraph) {
        this.successTransGraph = successTransGraph;
    }

    /**
     *
     * @return
     * The pDateGraph
     */
    public List<Integer> getPDateGraph() {
        return pDateGraph;
    }

    /**
     *
     * @param pDateGraph
     * The PDateGraph
     */
    public void setPDateGraph(List<Integer> pDateGraph) {
        this.pDateGraph = pDateGraph;
    }

    /**
     *
     * @return
     * The pViewGraph
     */
    public List<Integer> getPViewGraph() {
        return pViewGraph;
    }

    /**
     *
     * @param pViewGraph
     * The PViewGraph
     */
    public void setPViewGraph(List<Integer> pViewGraph) {
        this.pViewGraph = pViewGraph;
    }

    /**
     *
     * @return
     * The pProductSoldGraph
     */
    public List<Integer> getPProductSoldGraph() {
        return pProductSoldGraph;
    }

    /**
     *
     * @param pProductSoldGraph
     * The PProductSoldGraph
     */
    public void setPProductSoldGraph(List<Integer> pProductSoldGraph) {
        this.pProductSoldGraph = pProductSoldGraph;
    }

    /**
     *
     * @return
     * The pSuccessTransGraph
     */
    public List<Integer> getPSuccessTransGraph() {
        return pSuccessTransGraph;
    }

    /**
     *
     * @param pSuccessTransGraph
     * The PSuccessTransGraph
     */
    public void setPSuccessTransGraph(List<Integer> pSuccessTransGraph) {
        this.pSuccessTransGraph = pSuccessTransGraph;
    }

}
