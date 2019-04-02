package com.tokopedia.transaction.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderDetail implements Parcelable {
    private static final String TAG = OrderDetail.class.getSimpleName();

    @SerializedName("detail_insurance_price")
    @Expose
    private String detailInsurancePrice;
    @SerializedName("detail_open_amount")
    @Expose
    private String detailOpenAmount;
    @SerializedName("detail_dropship_name")
    @Expose
    private String detailDropshipName;
    @SerializedName("detail_total_add_fee")
    @Expose
    private String detailTotalAddFee;
    @SerializedName("detail_partial_order")
    @Expose
    private String detailPartialOrder;
    @SerializedName("detail_quantity")
    @Expose
    private String detailQuantity;
    @SerializedName("detail_product_price_idr")
    @Expose
    private String detailProductPriceIdr;
    @SerializedName("detail_invoice")
    @Expose
    private String detailInvoice;
    @SerializedName("detail_shipping_price_idr")
    @Expose
    private String detailShippingPriceIdr;
    @SerializedName("detail_pdf_path")
    @Expose
    private String detailPdfPath;
    @SerializedName("detail_additional_fee_idr")
    @Expose
    private String detailAdditionalFeeIdr;
    @SerializedName("detail_product_price")
    @Expose
    private String detailProductPrice;
    @SerializedName("detail_preorder")
    @Expose
    private DetailPreOrder detailPreorder;
    @SerializedName("detail_force_insurance")
    @Expose
    private String detailForceInsurance;
    @SerializedName("detail_open_amount_idr")
    @Expose
    private String detailOpenAmountIdr;
    @SerializedName("detail_additional_fee")
    @Expose
    private String detailAdditionalFee;
    @SerializedName("detail_dropship_telp")
    @Expose
    private String detailDropshipTelp;
    @SerializedName("detail_order_id")
    @Expose
    private String detailOrderId;
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
    private String detailTotalWeight;
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
    private String detailOrderStatus;

    @SerializedName("detail_free_return")
    @Expose
    private int detailFreeReturn;
    @SerializedName("detail_free_return_msg")
    @Expose
    private String detailFreeReturnMsg;

    @SerializedName("detail_complaint_popup_title")
    @Expose
    private String detailComplaintPopupTitle;

    @SerializedName("detail_complaint_popup_msg")
    @Expose
    private String detailComplaintPopupMsg;

    @SerializedName("detail_complaint_popup_msg_v2")
    @Expose
    private String detailComplaintPopupMsgV2;

    @SerializedName("detail_complaint_not_received_title")
    @Expose
    private String detailComplaintNotReceivedTitle;

    @SerializedName("detail_complaint_not_received_msg")
    @Expose
    private String detailComplaintNotReceivedMsg;

    @SerializedName("detail_finish_popup_title")
    @Expose
    private String detailFinishPopupTitle;
    @SerializedName("detail_finish_popup_msg")
    @Expose
    private String detailFinishPopupMsg;

    public String getDetailOpenAmount() {
        return detailOpenAmount;
    }

    public void setDetailOpenAmount(String detailOpenAmount) {
        this.detailOpenAmount = detailOpenAmount;
    }

    public String getDetailDropshipName() {
        return detailDropshipName;
    }

    public void setDetailDropshipName(String detailDropshipName) {
        this.detailDropshipName = detailDropshipName;
    }

    public String getDetailTotalAddFee() {
        return detailTotalAddFee;
    }

    public void setDetailTotalAddFee(String detailTotalAddFee) {
        this.detailTotalAddFee = detailTotalAddFee;
    }

    public String getDetailPartialOrder() {
        return detailPartialOrder;
    }

    public void setDetailPartialOrder(String detailPartialOrder) {
        this.detailPartialOrder = detailPartialOrder;
    }

    public String getDetailQuantity() {
        return detailQuantity;
    }

    public void setDetailQuantity(String detailQuantity) {
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

    public String getDetailPdfPath() {
        return detailPdfPath;
    }

    public void setDetailPdfPath(String detailPdfPath) {
        this.detailPdfPath = detailPdfPath;
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

    public DetailPreOrder getDetailPreorder() {
        return detailPreorder;
    }

    public void setDetailPreorder(DetailPreOrder detailPreorder) {
        this.detailPreorder = detailPreorder;
    }

    public String getDetailForceInsurance() {
        return detailForceInsurance;
    }

    public void setDetailForceInsurance(String detailForceInsurance) {
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

    public String getDetailDropshipTelp() {
        return detailDropshipTelp;
    }

    public void setDetailDropshipTelp(String detailDropshipTelp) {
        this.detailDropshipTelp = detailDropshipTelp;
    }

    public String getDetailOrderId() {
        return detailOrderId;
    }

    public void setDetailOrderId(String detailOrderId) {
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

    public String getDetailTotalWeight() {
        return detailTotalWeight;
    }

    public void setDetailTotalWeight(String detailTotalWeight) {
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

    public String getDetailOrderStatus() {
        return detailOrderStatus;
    }

    public void setDetailOrderStatus(String detailOrderStatus) {
        this.detailOrderStatus = detailOrderStatus;
    }

    public String getDetailInsurancePrice() {
        return detailInsurancePrice;
    }

    public void setDetailInsurancePrice(String detailInsurancePrice) {
        this.detailInsurancePrice = detailInsurancePrice;
    }

    public int getDetailFreeReturn() {
        return detailFreeReturn;
    }

    public void setDetailFreeReturn(int detailFreeReturn) {
        this.detailFreeReturn = detailFreeReturn;
    }

    public String getDetailFreeReturnMsg() {
        return detailFreeReturnMsg;
    }

    public void setDetailFreeReturnMsg(String detailFreeReturnMsg) {
        this.detailFreeReturnMsg = detailFreeReturnMsg;
    }

    public String getDetailComplaintPopupTitle() {
        return detailComplaintPopupTitle;
    }

    public void setDetailComplaintPopupTitle(String detailComplaintPopupTitle) {
        this.detailComplaintPopupTitle = detailComplaintPopupTitle;
    }

    public String getDetailComplaintPopupMsg() {
        return detailComplaintPopupMsg;
    }

    public void setDetailComplaintPopupMsg(String detailComplaintPopupMsg) {
        this.detailComplaintPopupMsg = detailComplaintPopupMsg;
    }

    public String getDetailComplaintPopupMsgV2() {
        return detailComplaintPopupMsgV2;
    }

    public void setDetailComplaintPopupMsgV2(String detailComplaintPopupMsgV2) {
        this.detailComplaintPopupMsgV2 = detailComplaintPopupMsgV2;
    }

    public String getDetailComplaintNotReceivedTitle() {
        return detailComplaintNotReceivedTitle;
    }

    public void setDetailComplaintNotReceivedTitle(String detailComplaintNotReceivedTitle) {
        this.detailComplaintNotReceivedTitle = detailComplaintNotReceivedTitle;
    }

    public String getDetailComplaintNotReceivedMsg() {
        return detailComplaintNotReceivedMsg;
    }

    public void setDetailComplaintNotReceivedMsg(String detailComplaintNotReceivedMsg) {
        this.detailComplaintNotReceivedMsg = detailComplaintNotReceivedMsg;
    }

    public String getDetailFinishPopupTitle() {
        return detailFinishPopupTitle;
    }

    public void setDetailFinishPopupTitle(String detailFinishPopupTitle) {
        this.detailFinishPopupTitle = detailFinishPopupTitle;
    }

    public String getDetailFinishPopupMsg() {
        return detailFinishPopupMsg;
    }

    public void setDetailFinishPopupMsg(String detailFinishPopupMsg) {
        this.detailFinishPopupMsg = detailFinishPopupMsg;
    }


    public OrderDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.detailInsurancePrice);
        dest.writeString(this.detailOpenAmount);
        dest.writeString(this.detailDropshipName);
        dest.writeString(this.detailTotalAddFee);
        dest.writeString(this.detailPartialOrder);
        dest.writeString(this.detailQuantity);
        dest.writeString(this.detailProductPriceIdr);
        dest.writeString(this.detailInvoice);
        dest.writeString(this.detailShippingPriceIdr);
        dest.writeString(this.detailPdfPath);
        dest.writeString(this.detailAdditionalFeeIdr);
        dest.writeString(this.detailProductPrice);
        dest.writeParcelable(this.detailPreorder, flags);
        dest.writeString(this.detailForceInsurance);
        dest.writeString(this.detailOpenAmountIdr);
        dest.writeString(this.detailAdditionalFee);
        dest.writeString(this.detailDropshipTelp);
        dest.writeString(this.detailOrderId);
        dest.writeString(this.detailTotalAddFeeIdr);
        dest.writeString(this.detailOrderDate);
        dest.writeString(this.detailShippingPrice);
        dest.writeString(this.detailPayDueDate);
        dest.writeString(this.detailTotalWeight);
        dest.writeString(this.detailInsurancePriceIdr);
        dest.writeString(this.detailPdfUri);
        dest.writeString(this.detailShipRefNum);
        dest.writeString(this.detailPrintAddressUri);
        dest.writeString(this.detailPdf);
        dest.writeString(this.detailOrderStatus);
        dest.writeInt(this.detailFreeReturn);
        dest.writeString(this.detailFreeReturnMsg);
        dest.writeString(this.detailComplaintPopupTitle);
        dest.writeString(this.detailComplaintPopupMsg);
        dest.writeString(this.detailComplaintPopupMsgV2);
        dest.writeString(this.detailComplaintNotReceivedTitle);
        dest.writeString(this.detailComplaintNotReceivedMsg);
        dest.writeString(this.detailFinishPopupTitle);
        dest.writeString(this.detailFinishPopupMsg);
    }

    protected OrderDetail(Parcel in) {
        this.detailInsurancePrice = in.readString();
        this.detailOpenAmount = in.readString();
        this.detailDropshipName = in.readString();
        this.detailTotalAddFee = in.readString();
        this.detailPartialOrder = in.readString();
        this.detailQuantity = in.readString();
        this.detailProductPriceIdr = in.readString();
        this.detailInvoice = in.readString();
        this.detailShippingPriceIdr = in.readString();
        this.detailPdfPath = in.readString();
        this.detailAdditionalFeeIdr = in.readString();
        this.detailProductPrice = in.readString();
        this.detailPreorder = in.readParcelable(DetailPreOrder.class.getClassLoader());
        this.detailForceInsurance = in.readString();
        this.detailOpenAmountIdr = in.readString();
        this.detailAdditionalFee = in.readString();
        this.detailDropshipTelp = in.readString();
        this.detailOrderId = in.readString();
        this.detailTotalAddFeeIdr = in.readString();
        this.detailOrderDate = in.readString();
        this.detailShippingPrice = in.readString();
        this.detailPayDueDate = in.readString();
        this.detailTotalWeight = in.readString();
        this.detailInsurancePriceIdr = in.readString();
        this.detailPdfUri = in.readString();
        this.detailShipRefNum = in.readString();
        this.detailPrintAddressUri = in.readString();
        this.detailPdf = in.readString();
        this.detailOrderStatus = in.readString();
        this.detailFreeReturn = in.readInt();
        this.detailFreeReturnMsg = in.readString();
        this.detailComplaintPopupTitle = in.readString();
        this.detailComplaintPopupMsg = in.readString();
        this.detailComplaintPopupMsgV2 = in.readString();
        this.detailComplaintNotReceivedTitle = in.readString();
        this.detailComplaintNotReceivedMsg = in.readString();
        this.detailFinishPopupTitle = in.readString();
        this.detailFinishPopupMsg = in.readString();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
