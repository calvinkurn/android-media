
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("detail_insurance_price")
    @Expose
    private String detailInsurancePrice;
    @SerializedName("detail_open_amount")
    @Expose
    private String detailOpenAmount;
    @SerializedName("detail_dropship_name")
    @Expose
    private int detailDropshipName;
    @SerializedName("detail_total_add_fee")
    @Expose
    private int detailTotalAddFee;
    @SerializedName("detail_partial_order")
    @Expose
    private String detailPartialOrder;
    @SerializedName("detail_quantity")
    @Expose
    private int detailQuantity;
    @SerializedName("detail_product_price_idr")
    @Expose
    private String detailProductPriceIdr;
    @SerializedName("detail_invoice")
    @Expose
    private String detailInvoice;
    @SerializedName("detail_shipping_price_idr")
    @Expose
    private String detailShippingPriceIdr;
    @SerializedName("detail_free_return")
    @Expose
    private int detailFreeReturn;
    @SerializedName("detail_pdf_path")
    @Expose
    private String detailPdfPath;
    @SerializedName("detail_free_return_msg")
    @Expose
    private String detailFreeReturnMsg;
    @SerializedName("detail_additional_fee_idr")
    @Expose
    private String detailAdditionalFeeIdr;
    @SerializedName("detail_product_price")
    @Expose
    private String detailProductPrice;
    @SerializedName("detail_preorder")
    @Expose
    private DetailPreorder detailPreorder;
    @SerializedName("detail_cancel_request")
    @Expose
    private DetailCancelRequest detailCancelRequest;
    @SerializedName("detail_force_insurance")
    @Expose
    private int detailForceInsurance;
    @SerializedName("detail_open_amount_idr")
    @Expose
    private String detailOpenAmountIdr;
    @SerializedName("detail_additional_fee")
    @Expose
    private String detailAdditionalFee;
    @SerializedName("detail_dropship_telp")
    @Expose
    private int detailDropshipTelp;
    @SerializedName("detail_order_id")
    @Expose
    private int detailOrderId;
    @SerializedName("detail_total_add_fee_idr")
    @Expose
    private String detailTotalAddFeeIdr;
    @SerializedName("detail_order_date")
    @Expose
    private String detailOrderDate;
    @SerializedName("detail_shipping_price")
    @Expose
    private String detailShippingPrice;
    @SerializedName("detail_pay_due_date")
    @Expose
    private String detailPayDueDate;
    @SerializedName("detail_total_weight")
    @Expose
    private double detailTotalWeight;
    @SerializedName("detail_insurance_price_idr")
    @Expose
    private String detailInsurancePriceIdr;
    @SerializedName("detail_pdf_uri")
    @Expose
    private String detailPdfUri;
    @SerializedName("detail_ship_ref_num")
    @Expose
    private String detailShipRefNum;
    @SerializedName("detail_print_address_uri")
    @Expose
    private String detailPrintAddressUri;
    @SerializedName("detail_pdf")
    @Expose
    private String detailPdf;
    @SerializedName("detail_order_status")
    @Expose
    private int detailOrderStatus;

    public String getDetailInsurancePrice() {
        return detailInsurancePrice;
    }

    public void setDetailInsurancePrice(String detailInsurancePrice) {
        this.detailInsurancePrice = detailInsurancePrice;
    }

    public String getDetailOpenAmount() {
        return detailOpenAmount;
    }

    public void setDetailOpenAmount(String detailOpenAmount) {
        this.detailOpenAmount = detailOpenAmount;
    }

    public int getDetailDropshipName() {
        return detailDropshipName;
    }

    public void setDetailDropshipName(int detailDropshipName) {
        this.detailDropshipName = detailDropshipName;
    }

    public int getDetailTotalAddFee() {
        return detailTotalAddFee;
    }

    public void setDetailTotalAddFee(int detailTotalAddFee) {
        this.detailTotalAddFee = detailTotalAddFee;
    }

    public String getDetailPartialOrder() {
        return detailPartialOrder;
    }

    public void setDetailPartialOrder(String detailPartialOrder) {
        this.detailPartialOrder = detailPartialOrder;
    }

    public int getDetailQuantity() {
        return detailQuantity;
    }

    public void setDetailQuantity(int detailQuantity) {
        this.detailQuantity = detailQuantity;
    }

    public String getDetailProductPriceIdr() {
        return detailProductPriceIdr;
    }

    public void setDetailProductPriceIdr(String detailProductPriceIdr) {
        this.detailProductPriceIdr = detailProductPriceIdr;
    }

    public String getDetailInvoice() {
        return detailInvoice;
    }

    public void setDetailInvoice(String detailInvoice) {
        this.detailInvoice = detailInvoice;
    }

    public String getDetailShippingPriceIdr() {
        return detailShippingPriceIdr;
    }

    public void setDetailShippingPriceIdr(String detailShippingPriceIdr) {
        this.detailShippingPriceIdr = detailShippingPriceIdr;
    }

    public int getDetailFreeReturn() {
        return detailFreeReturn;
    }

    public void setDetailFreeReturn(int detailFreeReturn) {
        this.detailFreeReturn = detailFreeReturn;
    }

    public String getDetailPdfPath() {
        return detailPdfPath;
    }

    public void setDetailPdfPath(String detailPdfPath) {
        this.detailPdfPath = detailPdfPath;
    }

    public String getDetailFreeReturnMsg() {
        return detailFreeReturnMsg;
    }

    public void setDetailFreeReturnMsg(String detailFreeReturnMsg) {
        this.detailFreeReturnMsg = detailFreeReturnMsg;
    }

    public String getDetailAdditionalFeeIdr() {
        return detailAdditionalFeeIdr;
    }

    public void setDetailAdditionalFeeIdr(String detailAdditionalFeeIdr) {
        this.detailAdditionalFeeIdr = detailAdditionalFeeIdr;
    }

    public String getDetailProductPrice() {
        return detailProductPrice;
    }

    public void setDetailProductPrice(String detailProductPrice) {
        this.detailProductPrice = detailProductPrice;
    }

    public DetailPreorder getDetailPreorder() {
        return detailPreorder;
    }

    public void setDetailPreorder(DetailPreorder detailPreorder) {
        this.detailPreorder = detailPreorder;
    }

    public DetailCancelRequest getDetailCancelRequest() {
        return detailCancelRequest;
    }

    public void setDetailCancelRequest(DetailCancelRequest detailCancelRequest) {
        this.detailCancelRequest = detailCancelRequest;
    }

    public int getDetailForceInsurance() {
        return detailForceInsurance;
    }

    public void setDetailForceInsurance(int detailForceInsurance) {
        this.detailForceInsurance = detailForceInsurance;
    }

    public String getDetailOpenAmountIdr() {
        return detailOpenAmountIdr;
    }

    public void setDetailOpenAmountIdr(String detailOpenAmountIdr) {
        this.detailOpenAmountIdr = detailOpenAmountIdr;
    }

    public String getDetailAdditionalFee() {
        return detailAdditionalFee;
    }

    public void setDetailAdditionalFee(String detailAdditionalFee) {
        this.detailAdditionalFee = detailAdditionalFee;
    }

    public int getDetailDropshipTelp() {
        return detailDropshipTelp;
    }

    public void setDetailDropshipTelp(int detailDropshipTelp) {
        this.detailDropshipTelp = detailDropshipTelp;
    }

    public int getDetailOrderId() {
        return detailOrderId;
    }

    public void setDetailOrderId(int detailOrderId) {
        this.detailOrderId = detailOrderId;
    }

    public String getDetailTotalAddFeeIdr() {
        return detailTotalAddFeeIdr;
    }

    public void setDetailTotalAddFeeIdr(String detailTotalAddFeeIdr) {
        this.detailTotalAddFeeIdr = detailTotalAddFeeIdr;
    }

    public String getDetailOrderDate() {
        return detailOrderDate;
    }

    public void setDetailOrderDate(String detailOrderDate) {
        this.detailOrderDate = detailOrderDate;
    }

    public String getDetailShippingPrice() {
        return detailShippingPrice;
    }

    public void setDetailShippingPrice(String detailShippingPrice) {
        this.detailShippingPrice = detailShippingPrice;
    }

    public String getDetailPayDueDate() {
        return detailPayDueDate;
    }

    public void setDetailPayDueDate(String detailPayDueDate) {
        this.detailPayDueDate = detailPayDueDate;
    }

    public double getDetailTotalWeight() {
        return detailTotalWeight;
    }

    public void setDetailTotalWeight(double detailTotalWeight) {
        this.detailTotalWeight = detailTotalWeight;
    }

    public String getDetailInsurancePriceIdr() {
        return detailInsurancePriceIdr;
    }

    public void setDetailInsurancePriceIdr(String detailInsurancePriceIdr) {
        this.detailInsurancePriceIdr = detailInsurancePriceIdr;
    }

    public String getDetailPdfUri() {
        return detailPdfUri;
    }

    public void setDetailPdfUri(String detailPdfUri) {
        this.detailPdfUri = detailPdfUri;
    }

    public String getDetailShipRefNum() {
        return detailShipRefNum;
    }

    public void setDetailShipRefNum(String detailShipRefNum) {
        this.detailShipRefNum = detailShipRefNum;
    }

    public String getDetailPrintAddressUri() {
        return detailPrintAddressUri;
    }

    public void setDetailPrintAddressUri(String detailPrintAddressUri) {
        this.detailPrintAddressUri = detailPrintAddressUri;
    }

    public String getDetailPdf() {
        return detailPdf;
    }

    public void setDetailPdf(String detailPdf) {
        this.detailPdf = detailPdf;
    }

    public int getDetailOrderStatus() {
        return detailOrderStatus;
    }

    public void setDetailOrderStatus(int detailOrderStatus) {
        this.detailOrderStatus = detailOrderStatus;
    }

}
