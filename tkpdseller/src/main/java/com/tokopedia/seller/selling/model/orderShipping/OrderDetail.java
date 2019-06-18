
package com.tokopedia.seller.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderDetail {

    @SerializedName("detail_insurance_price")
    @Expose
    String detailInsurancePrice;
    @SerializedName("detail_open_amount")
    @Expose
    String detailOpenAmount;
    @SerializedName("detail_dropship_name")
    @Expose
    String detailDropshipName;
    @SerializedName("detail_total_add_fee")
    @Expose
    Integer detailTotalAddFee;
    @SerializedName("detail_partial_order")
    @Expose
    String detailPartialOrder;
    @SerializedName("detail_quantity")
    @Expose
    String detailQuantity;
    @SerializedName("detail_product_price_idr")
    @Expose
    String detailProductPriceIdr;
    @SerializedName("detail_invoice")
    @Expose
    String detailInvoice;
    @SerializedName("detail_shipping_price_idr")
    @Expose
    String detailShippingPriceIdr;
    @SerializedName("detail_pdf_path")
    @Expose
    String detailPdfPath;
    @SerializedName("detail_additional_fee_idr")
    @Expose
    String detailAdditionalFeeIdr;
    @SerializedName("detail_product_price")
    @Expose
    String detailProductPrice;
    @SerializedName("detail_preorder")
    @Expose
    DetailPreorder detailPreorder;
    @SerializedName("detail_force_insurance")
    @Expose
    Integer detailForceInsurance;
    @SerializedName("detail_open_amount_idr")
    @Expose
    String detailOpenAmountIdr;
    @SerializedName("detail_additional_fee")
    @Expose
    Integer detailAdditionalFee;
    @SerializedName("detail_dropship_telp")
    @Expose
    String detailDropshipTelp;
    @SerializedName("detail_order_id")
    @Expose
    Integer detailOrderId;
    @SerializedName("detail_total_add_fee_idr")
    @Expose
    String detailTotalAddFeeIdr;
    @SerializedName("detail_order_date")
    @Expose
    String detailOrderDate;
    @SerializedName("detail_shipping_price")
    @Expose
    String detailShippingPrice;
    @SerializedName("detail_pay_due_date")
    @Expose
    String detailPayDueDate;
    @SerializedName("detail_total_weight")
    @Expose
    Double detailTotalWeight;
    @SerializedName("detail_insurance_price_idr")
    @Expose
    String detailInsurancePriceIdr;
    @SerializedName("detail_pdf_uri")
    @Expose
    String detailPdfUri;
    @SerializedName("detail_ship_ref_num")
    @Expose
    String detailShipRefNum;
    @SerializedName("detail_print_address_uri")
    @Expose
    String detailPrintAddressUri;
    @SerializedName("detail_pdf")
    @Expose
    String detailPdf;
    @SerializedName("detail_order_status")
    @Expose
    Integer detailOrderStatus;
    @SerializedName("detail_cancel_request")
    @Expose
    DetailCancelRequest detailCancelRequest;
    @SerializedName("warehouse_id")
    @Expose
    Integer warehouseId;
    @SerializedName("fulfill_by")
    @Expose
    Integer fulfillBy;

    /**
     *
     * @return
     * The detailCancelRequest
     */
    public DetailCancelRequest getDetailCancelRequest() {
        return detailCancelRequest;
    }

    /**
     *
     * @param detailCancelRequest
     * The detail_cancel_request
     */
    public void setDetailCancelRequest(DetailCancelRequest detailCancelRequest) {
        this.detailCancelRequest = detailCancelRequest;
    }

    /**
     * 
     * @return
     *     The detailInsurancePrice
     */
    public String getDetailInsurancePrice() {
        return detailInsurancePrice;
    }

    /**
     * 
     * @param detailInsurancePrice
     *     The detail_insurance_price
     */
    public void setDetailInsurancePrice(String detailInsurancePrice) {
        this.detailInsurancePrice = detailInsurancePrice;
    }

    /**
     * 
     * @return
     *     The detailOpenAmount
     */
    public String getDetailOpenAmount() {
        return detailOpenAmount;
    }

    /**
     * 
     * @param detailOpenAmount
     *     The detail_open_amount
     */
    public void setDetailOpenAmount(String detailOpenAmount) {
        this.detailOpenAmount = detailOpenAmount;
    }

    public String getDetailDropshipName() {
        return detailDropshipName;
    }

    public void setDetailDropshipName(String detailDropshipName) {
        this.detailDropshipName = detailDropshipName;
    }

    /**
     * 
     * @return
     *     The detailTotalAddFee
     */
    public Integer getDetailTotalAddFee() {
        return detailTotalAddFee;
    }

    /**
     * 
     * @param detailTotalAddFee
     *     The detail_total_add_fee
     */
    public void setDetailTotalAddFee(Integer detailTotalAddFee) {
        this.detailTotalAddFee = detailTotalAddFee;
    }

    /**
     * 
     * @return
     *     The detailPartialOrder
     */
    public String getDetailPartialOrder() {
        return detailPartialOrder;
    }

    /**
     * 
     * @param detailPartialOrder
     *     The detail_partial_order
     */
    public void setDetailPartialOrder(String detailPartialOrder) {
        this.detailPartialOrder = detailPartialOrder;
    }

    /**
     * 
     * @return
     *     The detailQuantity
     */
    public String getDetailQuantity() {
        return detailQuantity;
    }

    /**
     * 
     * @param detailQuantity
     *     The detail_quantity
     */
    public void setDetailQuantity(String detailQuantity) {
        this.detailQuantity = detailQuantity;
    }

    /**
     * 
     * @return
     *     The detailProductPriceIdr
     */
    public String getDetailProductPriceIdr() {
        return detailProductPriceIdr;
    }

    /**
     * 
     * @param detailProductPriceIdr
     *     The detail_product_price_idr
     */
    public void setDetailProductPriceIdr(String detailProductPriceIdr) {
        this.detailProductPriceIdr = detailProductPriceIdr;
    }

    /**
     * 
     * @return
     *     The detailInvoice
     */
    public String getDetailInvoice() {
        return detailInvoice;
    }

    /**
     * 
     * @param detailInvoice
     *     The detail_invoice
     */
    public void setDetailInvoice(String detailInvoice) {
        this.detailInvoice = detailInvoice;
    }

    /**
     * 
     * @return
     *     The detailShippingPriceIdr
     */
    public String getDetailShippingPriceIdr() {
        return detailShippingPriceIdr;
    }

    /**
     * 
     * @param detailShippingPriceIdr
     *     The detail_shipping_price_idr
     */
    public void setDetailShippingPriceIdr(String detailShippingPriceIdr) {
        this.detailShippingPriceIdr = detailShippingPriceIdr;
    }

    /**
     * 
     * @return
     *     The detailPdfPath
     */
    public String getDetailPdfPath() {
        return detailPdfPath;
    }

    /**
     * 
     * @param detailPdfPath
     *     The detail_pdf_path
     */
    public void setDetailPdfPath(String detailPdfPath) {
        this.detailPdfPath = detailPdfPath;
    }

    /**
     * 
     * @return
     *     The detailAdditionalFeeIdr
     */
    public String getDetailAdditionalFeeIdr() {
        return detailAdditionalFeeIdr;
    }

    /**
     * 
     * @param detailAdditionalFeeIdr
     *     The detail_additional_fee_idr
     */
    public void setDetailAdditionalFeeIdr(String detailAdditionalFeeIdr) {
        this.detailAdditionalFeeIdr = detailAdditionalFeeIdr;
    }

    /**
     * 
     * @return
     *     The detailProductPrice
     */
    public String getDetailProductPrice() {
        return detailProductPrice;
    }

    /**
     * 
     * @param detailProductPrice
     *     The detail_product_price
     */
    public void setDetailProductPrice(String detailProductPrice) {
        this.detailProductPrice = detailProductPrice;
    }

    /**
     * 
     * @return
     *     The detailPreorder
     */
    public DetailPreorder getDetailPreorder() {
        return detailPreorder;
    }

    /**
     * 
     * @param detailPreorder
     *     The detail_preorder
     */
    public void setDetailPreorder(DetailPreorder detailPreorder) {
        this.detailPreorder = detailPreorder;
    }

    /**
     * 
     * @return
     *     The detailForceInsurance
     */
    public Integer getDetailForceInsurance() {
        return detailForceInsurance;
    }

    /**
     * 
     * @param detailForceInsurance
     *     The detail_force_insurance
     */
    public void setDetailForceInsurance(Integer detailForceInsurance) {
        this.detailForceInsurance = detailForceInsurance;
    }

    /**
     * 
     * @return
     *     The detailOpenAmountIdr
     */
    public String getDetailOpenAmountIdr() {
        return detailOpenAmountIdr;
    }

    /**
     * 
     * @param detailOpenAmountIdr
     *     The detail_open_amount_idr
     */
    public void setDetailOpenAmountIdr(String detailOpenAmountIdr) {
        this.detailOpenAmountIdr = detailOpenAmountIdr;
    }

    /**
     * 
     * @return
     *     The detailAdditionalFee
     */
    public Integer getDetailAdditionalFee() {
        return detailAdditionalFee;
    }

    /**
     * 
     * @param detailAdditionalFee
     *     The detail_additional_fee
     */
    public void setDetailAdditionalFee(Integer detailAdditionalFee) {
        this.detailAdditionalFee = detailAdditionalFee;
    }

    public String getDetailDropshipTelp() {
        return detailDropshipTelp;
    }

    public void setDetailDropshipTelp(String detailDropshipTelp) {
        this.detailDropshipTelp = detailDropshipTelp;
    }

    /**
     * 
     * @return
     *     The detailOrderId
     */
    public Integer getDetailOrderId() {
        return detailOrderId;
    }

    /**
     * 
     * @param detailOrderId
     *     The detail_order_id
     */
    public void setDetailOrderId(Integer detailOrderId) {
        this.detailOrderId = detailOrderId;
    }

    /**
     * 
     * @return
     *     The detailTotalAddFeeIdr
     */
    public String getDetailTotalAddFeeIdr() {
        return detailTotalAddFeeIdr;
    }

    /**
     * 
     * @param detailTotalAddFeeIdr
     *     The detail_total_add_fee_idr
     */
    public void setDetailTotalAddFeeIdr(String detailTotalAddFeeIdr) {
        this.detailTotalAddFeeIdr = detailTotalAddFeeIdr;
    }

    /**
     * 
     * @return
     *     The detailOrderDate
     */
    public String getDetailOrderDate() {
        return detailOrderDate;
    }

    /**
     * 
     * @param detailOrderDate
     *     The detail_order_date
     */
    public void setDetailOrderDate(String detailOrderDate) {
        this.detailOrderDate = detailOrderDate;
    }

    /**
     * 
     * @return
     *     The detailShippingPrice
     */
    public String getDetailShippingPrice() {
        return detailShippingPrice;
    }

    /**
     * 
     * @param detailShippingPrice
     *     The detail_shipping_price
     */
    public void setDetailShippingPrice(String detailShippingPrice) {
        this.detailShippingPrice = detailShippingPrice;
    }

    /**
     * 
     * @return
     *     The detailPayDueDate
     */
    public String getDetailPayDueDate() {
        return detailPayDueDate;
    }

    /**
     * 
     * @param detailPayDueDate
     *     The detail_pay_due_date
     */
    public void setDetailPayDueDate(String detailPayDueDate) {
        this.detailPayDueDate = detailPayDueDate;
    }

    /**
     * 
     * @return
     *     The detailTotalWeight
     */
    public Double getDetailTotalWeight() {
        return detailTotalWeight;
    }

    /**
     * 
     * @param detailTotalWeight
     *     The detail_total_weight
     */
    public void setDetailTotalWeight(Double detailTotalWeight) {
        this.detailTotalWeight = detailTotalWeight;
    }

    /**
     * 
     * @return
     *     The detailInsurancePriceIdr
     */
    public String getDetailInsurancePriceIdr() {
        return detailInsurancePriceIdr;
    }

    /**
     * 
     * @param detailInsurancePriceIdr
     *     The detail_insurance_price_idr
     */
    public void setDetailInsurancePriceIdr(String detailInsurancePriceIdr) {
        this.detailInsurancePriceIdr = detailInsurancePriceIdr;
    }

    /**
     * 
     * @return
     *     The detailPdfUri
     */
    public String getDetailPdfUri() {
        return detailPdfUri;
    }

    /**
     * 
     * @param detailPdfUri
     *     The detail_pdf_uri
     */
    public void setDetailPdfUri(String detailPdfUri) {
        this.detailPdfUri = detailPdfUri;
    }

    /**
     * 
     * @return
     *     The detailShipRefNum
     */
    public String getDetailShipRefNum() {
        return detailShipRefNum;
    }

    /**
     * 
     * @param detailShipRefNum
     *     The detail_ship_ref_num
     */
    public void setDetailShipRefNum(String detailShipRefNum) {
        this.detailShipRefNum = detailShipRefNum;
    }

    /**
     * 
     * @return
     *     The detailPrintAddressUri
     */
    public String getDetailPrintAddressUri() {
        return detailPrintAddressUri;
    }

    /**
     * 
     * @param detailPrintAddressUri
     *     The detail_print_address_uri
     */
    public void setDetailPrintAddressUri(String detailPrintAddressUri) {
        this.detailPrintAddressUri = detailPrintAddressUri;
    }

    /**
     * 
     * @return
     *     The detailPdf
     */
    public String getDetailPdf() {
        return detailPdf;
    }

    /**
     * 
     * @param detailPdf
     *     The detail_pdf
     */
    public void setDetailPdf(String detailPdf) {
        this.detailPdf = detailPdf;
    }

    /**
     * 
     * @return
     *     The detailOrderStatus
     */
    public Integer getDetailOrderStatus() {
        return detailOrderStatus;
    }

    /**
     * 
     * @param detailOrderStatus
     *     The detail_order_status
     */
    public void setDetailOrderStatus(Integer detailOrderStatus) {
        this.detailOrderStatus = detailOrderStatus;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getFulfillBy() {
        return fulfillBy;
    }

    public void setFulfillBy(Integer fulfillBy) {
        this.fulfillBy = fulfillBy;
    }
}
