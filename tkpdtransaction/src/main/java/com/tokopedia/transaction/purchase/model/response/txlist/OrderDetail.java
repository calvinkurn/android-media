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

    protected OrderDetail(Parcel in) {
        detailInsurancePrice = in.readString();
        detailOpenAmount = in.readString();
        detailDropshipName = in.readString();
        detailTotalAddFee = in.readString();
        detailPartialOrder = in.readString();
        detailQuantity = in.readString();
        detailProductPriceIdr = in.readString();
        detailInvoice = in.readString();
        detailShippingPriceIdr = in.readString();
        detailPdfPath = in.readString();
        detailAdditionalFeeIdr = in.readString();
        detailProductPrice = in.readString();
        detailPreorder = in.readParcelable(DetailPreOrder.class.getClassLoader());
        detailForceInsurance = in.readString();
        detailOpenAmountIdr = in.readString();
        detailAdditionalFee = in.readString();
        detailDropshipTelp = in.readString();
        detailOrderId = in.readString();
        detailTotalAddFeeIdr = in.readString();
        detailOrderDate = in.readString();
        detailShippingPrice = in.readString();
        detailPayDueDate = in.readString();
        detailTotalWeight = in.readString();
        detailInsurancePriceIdr = in.readString();
        detailPdfUri = in.readString();
        detailShipRefNum = in.readString();
        detailPrintAddressUri = in.readString();
        detailPdf = in.readString();
        detailOrderStatus = in.readString();
        detailFreeReturn = in.readInt();
        detailFreeReturnMsg = in.readString();
        detailComplaintPopupTitle = in.readString();
        detailComplaintPopupMsg = in.readString();
        detailComplaintNotReceivedTitle = in.readString();
        detailComplaintNotReceivedMsg = in.readString();
        detailFinishPopupTitle = in.readString();
        detailFinishPopupMsg = in.readString();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };

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
        dest.writeString(detailInsurancePrice);
        dest.writeString(detailOpenAmount);
        dest.writeString(detailDropshipName);
        dest.writeString(detailTotalAddFee);
        dest.writeString(detailPartialOrder);
        dest.writeString(detailQuantity);
        dest.writeString(detailProductPriceIdr);
        dest.writeString(detailInvoice);
        dest.writeString(detailShippingPriceIdr);
        dest.writeString(detailPdfPath);
        dest.writeString(detailAdditionalFeeIdr);
        dest.writeString(detailProductPrice);
        dest.writeParcelable(detailPreorder, flags);
        dest.writeString(detailForceInsurance);
        dest.writeString(detailOpenAmountIdr);
        dest.writeString(detailAdditionalFee);
        dest.writeString(detailDropshipTelp);
        dest.writeString(detailOrderId);
        dest.writeString(detailTotalAddFeeIdr);
        dest.writeString(detailOrderDate);
        dest.writeString(detailShippingPrice);
        dest.writeString(detailPayDueDate);
        dest.writeString(detailTotalWeight);
        dest.writeString(detailInsurancePriceIdr);
        dest.writeString(detailPdfUri);
        dest.writeString(detailShipRefNum);
        dest.writeString(detailPrintAddressUri);
        dest.writeString(detailPdf);
        dest.writeString(detailOrderStatus);
        dest.writeInt(detailFreeReturn);
        dest.writeString(detailFreeReturnMsg);
        dest.writeString(detailComplaintPopupTitle);
        dest.writeString(detailComplaintPopupMsg);
        dest.writeString(detailComplaintNotReceivedTitle);
        dest.writeString(detailComplaintNotReceivedMsg);
        dest.writeString(detailFinishPopupTitle);
        dest.writeString(detailFinishPopupMsg);
    }
}
