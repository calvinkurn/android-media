package com.tokopedia.inbox.rescenter.edit.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/24/16.
 */
public class EditResCenterFormData implements Parcelable {

    @SerializedName("form")
    private Form form;
    @SerializedName("list_ts")
    private List<TroubleCategoryData> listTs;
    @SerializedName("list")
    private List<ProductData> listProd;
    @SerializedName("form_solution")
    private List<SolutionData> listSolution;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<TroubleCategoryData> getListTs() {
        return listTs;
    }

    public void setListTs(List<TroubleCategoryData> listTs) {
        this.listTs = listTs;
    }

    public List<ProductData> getListProd() {
        return listProd;
    }

    public void setListProd(List<ProductData> listProd) {
        this.listProd = listProd;
    }

    public List<SolutionData> getListSolution() {
        return listSolution;
    }

    public void setListSolution(List<SolutionData> listSolution) {
        this.listSolution = listSolution;
    }

    public static class Form implements Parcelable {

        @SerializedName("resolution_last")
        private ResolutionLast resolutionLast;
        @SerializedName("resolution_order")
        private ResolutionOrder resolutionOrder;
        @SerializedName("resolution_solution_list")
        private List<SolutionData> resolutionSolutionList;

        public ResolutionLast getResolutionLast() {
            return resolutionLast;
        }

        public void setResolutionLast(ResolutionLast resolutionLast) {
            this.resolutionLast = resolutionLast;
        }

        public ResolutionOrder getResolutionOrder() {
            return resolutionOrder;
        }

        public void setResolutionOrder(ResolutionOrder resolutionOrder) {
            this.resolutionOrder = resolutionOrder;
        }

        public List<SolutionData> getResolutionSolutionList() {
            return resolutionSolutionList;
        }

        public void setResolutionSolutionList(List<SolutionData> resolutionSolutionList) {
            this.resolutionSolutionList = resolutionSolutionList;
        }

        public Form() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.resolutionLast, flags);
            dest.writeParcelable(this.resolutionOrder, flags);
            dest.writeTypedList(this.resolutionSolutionList);
        }

        protected Form(Parcel in) {
            this.resolutionLast = in.readParcelable(ResolutionLast.class.getClassLoader());
            this.resolutionOrder = in.readParcelable(ResolutionOrder.class.getClassLoader());
            this.resolutionSolutionList = in.createTypedArrayList(SolutionData.CREATOR);
        }

        public static final Creator<Form> CREATOR = new Creator<Form>() {
            @Override
            public Form createFromParcel(Parcel source) {
                return new Form(source);
            }

            @Override
            public Form[] newArray(int size) {
                return new Form[size];
            }
        };
    }

    public static class ResolutionLast implements Parcelable {
        @SerializedName("last_trouble_string")
        private String lastTroubleString;
        @SerializedName("last_resolution_id")
        private Integer lastResolutionId;
        @SerializedName("last_show_input_addr_button")
        private Integer lastShowInputAddrButton;
        @SerializedName("last_show_appeal_button")
        private Integer lastShowAppealButton;
        @SerializedName("last_refund_amt")
        private Integer lastRefundAmt;
        @SerializedName("last_solution")
        private Integer lastSolution;
        @SerializedName("last_show_input_resi_button")
        private Integer lastShowInputResiButton;
        @SerializedName("last_show_accept_button")
        private Integer lastShowAcceptButton;
        @SerializedName("last_product_related")
        private Integer lastProductRelated;
        @SerializedName("last_action_by")
        private Integer lastActionBy;
        @SerializedName("last_refund_amt_idr")
        private String lastRefundAmtIdr;
        @SerializedName("last_rival_accepted")
        private Integer lastRivalAccepted;
        @SerializedName("last_show_finish_button")
        private Integer lastShowFinishButton;
        @SerializedName("last_trouble_type")
        private Integer lastTroubleType;
        @SerializedName("last_show_accept_admin_button")
        private Integer lastShowAcceptAdminButton;
        @SerializedName("last_flag_received")
        private Integer lastFlagReceived;
        @SerializedName("last_solution_string")
        private String lastSolutionString;
        @SerializedName("last_category_trouble_type")
        private Integer lastCategoryTroubleType;
        @SerializedName("last_category_trouble_string")
        private String lastCategoryTroubleString;
        @SerializedName("last_product_trouble")
        private List<LastProductTrouble> lastProductTrouble;
        @SerializedName("last_solution_remark")
        private String lastSolutionRemark;

        public String getLastTroubleString() {
            return lastTroubleString;
        }

        public void setLastTroubleString(String lastTroubleString) {
            this.lastTroubleString = lastTroubleString;
        }

        public Integer getLastResolutionId() {
            return lastResolutionId;
        }

        public void setLastResolutionId(Integer lastResolutionId) {
            this.lastResolutionId = lastResolutionId;
        }

        public Integer getLastShowInputAddrButton() {
            return lastShowInputAddrButton;
        }

        public void setLastShowInputAddrButton(Integer lastShowInputAddrButton) {
            this.lastShowInputAddrButton = lastShowInputAddrButton;
        }

        public Integer getLastShowAppealButton() {
            return lastShowAppealButton;
        }

        public void setLastShowAppealButton(Integer lastShowAppealButton) {
            this.lastShowAppealButton = lastShowAppealButton;
        }

        public Integer getLastRefundAmt() {
            return lastRefundAmt;
        }

        public void setLastRefundAmt(Integer lastRefundAmt) {
            this.lastRefundAmt = lastRefundAmt;
        }

        public Integer getLastSolution() {
            return lastSolution;
        }

        public void setLastSolution(Integer lastSolution) {
            this.lastSolution = lastSolution;
        }

        public Integer getLastShowInputResiButton() {
            return lastShowInputResiButton;
        }

        public void setLastShowInputResiButton(Integer lastShowInputResiButton) {
            this.lastShowInputResiButton = lastShowInputResiButton;
        }

        public Integer getLastShowAcceptButton() {
            return lastShowAcceptButton;
        }

        public void setLastShowAcceptButton(Integer lastShowAcceptButton) {
            this.lastShowAcceptButton = lastShowAcceptButton;
        }

        public Integer getLastProductRelated() {
            return lastProductRelated;
        }

        public void setLastProductRelated(Integer lastProductRelated) {
            this.lastProductRelated = lastProductRelated;
        }

        public Integer getLastActionBy() {
            return lastActionBy;
        }

        public void setLastActionBy(Integer lastActionBy) {
            this.lastActionBy = lastActionBy;
        }

        public String getLastRefundAmtIdr() {
            return lastRefundAmtIdr;
        }

        public void setLastRefundAmtIdr(String lastRefundAmtIdr) {
            this.lastRefundAmtIdr = lastRefundAmtIdr;
        }

        public Integer getLastRivalAccepted() {
            return lastRivalAccepted;
        }

        public void setLastRivalAccepted(Integer lastRivalAccepted) {
            this.lastRivalAccepted = lastRivalAccepted;
        }

        public Integer getLastShowFinishButton() {
            return lastShowFinishButton;
        }

        public void setLastShowFinishButton(Integer lastShowFinishButton) {
            this.lastShowFinishButton = lastShowFinishButton;
        }

        public Integer getLastTroubleType() {
            return lastTroubleType;
        }

        public void setLastTroubleType(Integer lastTroubleType) {
            this.lastTroubleType = lastTroubleType;
        }

        public Integer getLastShowAcceptAdminButton() {
            return lastShowAcceptAdminButton;
        }

        public void setLastShowAcceptAdminButton(Integer lastShowAcceptAdminButton) {
            this.lastShowAcceptAdminButton = lastShowAcceptAdminButton;
        }

        public Integer getLastFlagReceived() {
            return lastFlagReceived;
        }

        public void setLastFlagReceived(Integer lastFlagReceived) {
            this.lastFlagReceived = lastFlagReceived;
        }

        public String getLastSolutionString() {
            return lastSolutionString;
        }

        public void setLastSolutionString(String lastSolutionString) {
            this.lastSolutionString = lastSolutionString;
        }

        public String getLastCategoryTroubleString() {
            return lastCategoryTroubleString;
        }

        public void setLastCategoryTroubleString(String lastCategoryTroubleString) {
            this.lastCategoryTroubleString = lastCategoryTroubleString;
        }

        public Integer getLastCategoryTroubleType() {
            return lastCategoryTroubleType;
        }

        public void setLastCategoryTroubleType(Integer lastCategoryTroubleType) {
            this.lastCategoryTroubleType = lastCategoryTroubleType;
        }

        public List<LastProductTrouble> getLastProductTrouble() {
            return lastProductTrouble;
        }

        public void setLastProductTrouble(List<LastProductTrouble> lastProductTrouble) {
            this.lastProductTrouble = lastProductTrouble;
        }

        public ResolutionLast() {
        }

        public void setLastSolutionRemark(String lastSolutionRemark) {
            this.lastSolutionRemark = lastSolutionRemark;
        }

        public String getLastSolutionRemark() {
            return lastSolutionRemark;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.lastTroubleString);
            dest.writeValue(this.lastResolutionId);
            dest.writeValue(this.lastShowInputAddrButton);
            dest.writeValue(this.lastShowAppealButton);
            dest.writeValue(this.lastRefundAmt);
            dest.writeValue(this.lastSolution);
            dest.writeValue(this.lastShowInputResiButton);
            dest.writeValue(this.lastShowAcceptButton);
            dest.writeValue(this.lastProductRelated);
            dest.writeValue(this.lastActionBy);
            dest.writeString(this.lastRefundAmtIdr);
            dest.writeValue(this.lastRivalAccepted);
            dest.writeValue(this.lastShowFinishButton);
            dest.writeValue(this.lastTroubleType);
            dest.writeValue(this.lastShowAcceptAdminButton);
            dest.writeValue(this.lastFlagReceived);
            dest.writeString(this.lastSolutionString);
            dest.writeValue(this.lastCategoryTroubleType);
            dest.writeString(this.lastCategoryTroubleString);
            dest.writeTypedList(this.lastProductTrouble);
            dest.writeString(this.lastSolutionRemark);
        }

        protected ResolutionLast(Parcel in) {
            this.lastTroubleString = in.readString();
            this.lastResolutionId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowInputAddrButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAppealButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmt = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastSolution = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowInputResiButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAcceptButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastProductRelated = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastActionBy = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmtIdr = in.readString();
            this.lastRivalAccepted = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowFinishButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAcceptAdminButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastFlagReceived = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastSolutionString = in.readString();
            this.lastCategoryTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastCategoryTroubleString = in.readString();
            this.lastProductTrouble = in.createTypedArrayList(LastProductTrouble.CREATOR);
            this.lastSolutionRemark = in.readString();
        }

        public static final Creator<ResolutionLast> CREATOR = new Creator<ResolutionLast>() {
            @Override
            public ResolutionLast createFromParcel(Parcel source) {
                return new ResolutionLast(source);
            }

            @Override
            public ResolutionLast[] newArray(int size) {
                return new ResolutionLast[size];
            }
        };
    }

    public static class LastProductTrouble implements Parcelable {
        @SerializedName("pt_snapshot_uri")
        private String ptSnapshotUri;
        @SerializedName("pt_product_name")
        private String ptProductName;
        @SerializedName("pt_trouble_name")
        private String ptTroubleName;
        @SerializedName("pt_trouble_id")
        private int ptTroubleId;
        @SerializedName("pt_show_input_quantity")
        private int ptShowInputQuantity;
        @SerializedName("pt_product_id")
        private String ptProductId;
        @SerializedName("pt_solution_remark")
        private String ptSolutionRemark;
        @SerializedName("pt_order_dtl_id")
        private String ptOrderDtlId;
        @SerializedName("pt_quantity")
        private int ptQuantity;
        @SerializedName("pt_free_return")
        private int ptFreeReturn;

        public String getPtSnapshotUri() {
            return ptSnapshotUri;
        }

        public void setPtSnapshotUri(String ptSnapshotUri) {
            this.ptSnapshotUri = ptSnapshotUri;
        }

        public String getPtProductName() {
            return ptProductName;
        }

        public void setPtProductName(String ptProductName) {
            this.ptProductName = ptProductName;
        }

        public String getPtTroubleName() {
            return ptTroubleName;
        }

        public void setPtTroubleName(String ptTroubleName) {
            this.ptTroubleName = ptTroubleName;
        }

        public int getPtTroubleId() {
            return ptTroubleId;
        }

        public void setPtTroubleId(int ptTroubleId) {
            this.ptTroubleId = ptTroubleId;
        }

        public int getPtShowInputQuantity() {
            return ptShowInputQuantity;
        }

        public void setPtShowInputQuantity(int ptShowInputQuantity) {
            this.ptShowInputQuantity = ptShowInputQuantity;
        }

        public String getPtProductId() {
            return ptProductId;
        }

        public void setPtProductId(String ptProductId) {
            this.ptProductId = ptProductId;
        }

        public String getPtSolutionRemark() {
            return ptSolutionRemark;
        }

        public void setPtSolutionRemark(String ptSolutionRemark) {
            this.ptSolutionRemark = ptSolutionRemark;
        }

        public String getPtOrderDtlId() {
            return ptOrderDtlId;
        }

        public void setPtOrderDtlId(String ptOrderDtlId) {
            this.ptOrderDtlId = ptOrderDtlId;
        }

        public int getPtQuantity() {
            return ptQuantity;
        }

        public void setPtQuantity(int ptQuantity) {
            this.ptQuantity = ptQuantity;
        }

        public int getPtFreeReturn() {
            return ptFreeReturn;
        }

        public void setPtFreeReturn(int ptFreeReturn) {
            this.ptFreeReturn = ptFreeReturn;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ptSnapshotUri);
            dest.writeString(this.ptProductName);
            dest.writeString(this.ptTroubleName);
            dest.writeInt(this.ptTroubleId);
            dest.writeInt(this.ptShowInputQuantity);
            dest.writeString(this.ptProductId);
            dest.writeString(this.ptSolutionRemark);
            dest.writeString(this.ptOrderDtlId);
            dest.writeInt(this.ptQuantity);
            dest.writeInt(this.ptFreeReturn);
        }

        public LastProductTrouble() {
        }

        protected LastProductTrouble(Parcel in) {
            this.ptSnapshotUri = in.readString();
            this.ptProductName = in.readString();
            this.ptTroubleName = in.readString();
            this.ptTroubleId = in.readInt();
            this.ptShowInputQuantity = in.readInt();
            this.ptProductId = in.readString();
            this.ptSolutionRemark = in.readString();
            this.ptOrderDtlId = in.readString();
            this.ptQuantity = in.readInt();
            this.ptFreeReturn = in.readInt();
        }

        public static final Creator<LastProductTrouble> CREATOR = new Creator<LastProductTrouble>() {
            @Override
            public LastProductTrouble createFromParcel(Parcel source) {
                return new LastProductTrouble(source);
            }

            @Override
            public LastProductTrouble[] newArray(int size) {
                return new LastProductTrouble[size];
            }
        };
    }

    public static class ResolutionOrder implements Parcelable {
        @SerializedName("order_shipping_fee_idr")
        private String orderShippingFeeIdr;
        @SerializedName("order_shop_url")
        private String orderShopUrl;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("order_open_amount")
        private Integer orderOpenAmount;
        @SerializedName("order_pdf_url")
        private String orderPdfUrl;
        @SerializedName("order_shipping_fee")
        private Integer orderShippingFee;
        @SerializedName("order_open_amount_idr")
        private String orderOpenAmountIdr;
        @SerializedName("order_product_fee")
        private Integer orderProductFee;
        @SerializedName("order_shop_name")
        private String orderShopName;
        @SerializedName("order_is_customer")
        private Integer orderIsCustomer;
        @SerializedName("order_product_fee_idr")
        private String orderProductFeeIdr;
        @SerializedName("order_free_return")
        private Integer orderFreeReturn;
        @SerializedName("order_invoice_ref_num")
        private String orderInvoiceRefNum;

        public String getOrderShippingFeeIdr() {
            return orderShippingFeeIdr;
        }

        public void setOrderShippingFeeIdr(String orderShippingFeeIdr) {
            this.orderShippingFeeIdr = orderShippingFeeIdr;
        }

        public String getOrderShopUrl() {
            return orderShopUrl;
        }

        public void setOrderShopUrl(String orderShopUrl) {
            this.orderShopUrl = orderShopUrl;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getOrderOpenAmount() {
            return orderOpenAmount;
        }

        public void setOrderOpenAmount(Integer orderOpenAmount) {
            this.orderOpenAmount = orderOpenAmount;
        }

        public String getOrderPdfUrl() {
            return orderPdfUrl;
        }

        public void setOrderPdfUrl(String orderPdfUrl) {
            this.orderPdfUrl = orderPdfUrl;
        }

        public Integer getOrderShippingFee() {
            return orderShippingFee;
        }

        public void setOrderShippingFee(Integer orderShippingFee) {
            this.orderShippingFee = orderShippingFee;
        }

        public String getOrderOpenAmountIdr() {
            return orderOpenAmountIdr;
        }

        public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
            this.orderOpenAmountIdr = orderOpenAmountIdr;
        }

        public Integer getOrderProductFee() {
            return orderProductFee;
        }

        public void setOrderProductFee(Integer orderProductFee) {
            this.orderProductFee = orderProductFee;
        }

        public String getOrderShopName() {
            return orderShopName;
        }

        public void setOrderShopName(String orderShopName) {
            this.orderShopName = orderShopName;
        }

        public Integer getOrderIsCustomer() {
            return orderIsCustomer;
        }

        public void setOrderIsCustomer(Integer orderIsCustomer) {
            this.orderIsCustomer = orderIsCustomer;
        }

        public String getOrderProductFeeIdr() {
            return orderProductFeeIdr;
        }

        public void setOrderProductFeeIdr(String orderProductFeeIdr) {
            this.orderProductFeeIdr = orderProductFeeIdr;
        }

        public Integer getOrderFreeReturn() {
            return orderFreeReturn;
        }

        public void setOrderFreeReturn(Integer orderFreeReturn) {
            this.orderFreeReturn = orderFreeReturn;
        }

        public String getOrderInvoiceRefNum() {
            return orderInvoiceRefNum;
        }

        public void setOrderInvoiceRefNum(String orderInvoiceRefNum) {
            this.orderInvoiceRefNum = orderInvoiceRefNum;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderShippingFeeIdr);
            dest.writeString(this.orderShopUrl);
            dest.writeString(this.orderId);
            dest.writeValue(this.orderOpenAmount);
            dest.writeString(this.orderPdfUrl);
            dest.writeValue(this.orderShippingFee);
            dest.writeString(this.orderOpenAmountIdr);
            dest.writeValue(this.orderProductFee);
            dest.writeString(this.orderShopName);
            dest.writeValue(this.orderIsCustomer);
            dest.writeString(this.orderProductFeeIdr);
            dest.writeValue(this.orderFreeReturn);
            dest.writeString(this.orderInvoiceRefNum);
        }

        public ResolutionOrder() {
        }

        protected ResolutionOrder(Parcel in) {
            this.orderShippingFeeIdr = in.readString();
            this.orderShopUrl = in.readString();
            this.orderId = in.readString();
            this.orderOpenAmount = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderPdfUrl = in.readString();
            this.orderShippingFee = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderOpenAmountIdr = in.readString();
            this.orderProductFee = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderShopName = in.readString();
            this.orderIsCustomer = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderProductFeeIdr = in.readString();
            this.orderFreeReturn = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderInvoiceRefNum = in.readString();
        }

        public static final Creator<ResolutionOrder> CREATOR = new Creator<ResolutionOrder>() {
            @Override
            public ResolutionOrder createFromParcel(Parcel source) {
                return new ResolutionOrder(source);
            }

            @Override
            public ResolutionOrder[] newArray(int size) {
                return new ResolutionOrder[size];
            }
        };
    }

    public static class TroubleCategoryData implements Parcelable {
        @SerializedName("attachment")
        private Integer attachment;
        @SerializedName("product_is_received")
        private Integer productIsReceived;
        @SerializedName("product_related")
        private Integer productRelated;
        @SerializedName("category_trouble_id")
        private String categoryTroubleId;
        @SerializedName("category_trouble_text")
        private String categoryTroubleText;
        @SerializedName("possible_solution")
        private List<PossibleSolution> possibleSolution;
        @SerializedName("trouble_list")
        private List<TroubleData> troubleList;
        @SerializedName("trouble_list_fr")
        private List<TroubleData> troubleListFreeReturn;

        public Integer getAttachment() {
            return attachment;
        }

        public void setAttachment(Integer attachment) {
            this.attachment = attachment;
        }

        public Integer getProductIsReceived() {
            return productIsReceived;
        }

        public void setProductIsReceived(Integer productIsReceived) {
            this.productIsReceived = productIsReceived;
        }

        public Integer getProductRelated() {
            return productRelated;
        }

        public void setProductRelated(Integer productRelated) {
            this.productRelated = productRelated;
        }

        public String getCategoryTroubleId() {
            return categoryTroubleId;
        }

        public void setCategoryTroubleId(String categoryTroubleId) {
            this.categoryTroubleId = categoryTroubleId;
        }

        public String getCategoryTroubleText() {
            return categoryTroubleText;
        }

        public void setCategoryTroubleText(String categoryTroubleText) {
            this.categoryTroubleText = categoryTroubleText;
        }

        public List<PossibleSolution> getPossibleSolution() {
            return possibleSolution;
        }

        public void setPossibleSolution(List<PossibleSolution> possibleSolution) {
            this.possibleSolution = possibleSolution;
        }

        public List<TroubleData> getTroubleList() {
            return troubleList;
        }

        public void setTroubleList(List<TroubleData> troubleList) {
            this.troubleList = troubleList;
        }

        public List<TroubleData> getTroubleListFreeReturn() {
            return troubleListFreeReturn;
        }

        public void setTroubleListFreeReturn(List<TroubleData> troubleListFreeReturn) {
            this.troubleListFreeReturn = troubleListFreeReturn;
        }

        public TroubleCategoryData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.attachment);
            dest.writeValue(this.productIsReceived);
            dest.writeValue(this.productRelated);
            dest.writeString(this.categoryTroubleId);
            dest.writeString(this.categoryTroubleText);
            dest.writeTypedList(this.possibleSolution);
            dest.writeTypedList(this.troubleList);
            dest.writeTypedList(this.troubleListFreeReturn);
        }

        protected TroubleCategoryData(Parcel in) {
            this.attachment = (Integer) in.readValue(Integer.class.getClassLoader());
            this.productIsReceived = (Integer) in.readValue(Integer.class.getClassLoader());
            this.productRelated = (Integer) in.readValue(Integer.class.getClassLoader());
            this.categoryTroubleId = in.readString();
            this.categoryTroubleText = in.readString();
            this.possibleSolution = in.createTypedArrayList(PossibleSolution.CREATOR);
            this.troubleList = in.createTypedArrayList(TroubleData.CREATOR);
            this.troubleListFreeReturn = in.createTypedArrayList(TroubleData.CREATOR);
        }

        public static final Creator<TroubleCategoryData> CREATOR = new Creator<TroubleCategoryData>() {
            @Override
            public TroubleCategoryData createFromParcel(Parcel source) {
                return new TroubleCategoryData(source);
            }

            @Override
            public TroubleCategoryData[] newArray(int size) {
                return new TroubleCategoryData[size];
            }
        };
    }

    public static class PossibleSolution implements Parcelable {
        @SerializedName("solution_text")
        private String solutionText;
        @SerializedName("solution_id")
        private String solutionId;
        @SerializedName("possible_trouble")
        private List<PossibleTrouble> possibleTrouble;

        public String getSolutionText() {
            return solutionText;
        }

        public void setSolutionText(String solutionText) {
            this.solutionText = solutionText;
        }

        public String getSolutionId() {
            return solutionId;
        }

        public void setSolutionId(String solutionId) {
            this.solutionId = solutionId;
        }

        public List<PossibleTrouble> getPossibleTrouble() {
            return possibleTrouble;
        }

        public void setPossibleTrouble(List<PossibleTrouble> possibleTrouble) {
            this.possibleTrouble = possibleTrouble;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.solutionText);
            dest.writeString(this.solutionId);
            dest.writeList(this.possibleTrouble);
        }

        public PossibleSolution() {
        }

        protected PossibleSolution(Parcel in) {
            this.solutionText = in.readString();
            this.solutionId = in.readString();
            this.possibleTrouble = new ArrayList<PossibleTrouble>();
            in.readList(this.possibleTrouble, PossibleTrouble.class.getClassLoader());
        }

        public static final Creator<PossibleSolution> CREATOR = new Creator<PossibleSolution>() {
            @Override
            public PossibleSolution createFromParcel(Parcel source) {
                return new PossibleSolution(source);
            }

            @Override
            public PossibleSolution[] newArray(int size) {
                return new PossibleSolution[size];
            }
        };
    }

    public static class PossibleTrouble implements Parcelable {
        @SerializedName("refund_type")
        private Integer refundType;
        @SerializedName("trouble_text")
        private String troubleText;
        @SerializedName("trouble_id")
        private String troubleId;

        public Integer getRefundType() {
            return refundType;
        }

        public void setRefundType(Integer refundType) {
            this.refundType = refundType;
        }

        public String getTroubleText() {
            return troubleText;
        }

        public void setTroubleText(String troubleText) {
            this.troubleText = troubleText;
        }

        public String getTroubleId() {
            return troubleId;
        }

        public void setTroubleId(String troubleId) {
            this.troubleId = troubleId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.refundType);
            dest.writeString(this.troubleText);
            dest.writeString(this.troubleId);
        }

        public PossibleTrouble() {
        }

        protected PossibleTrouble(Parcel in) {
            this.refundType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.troubleText = in.readString();
            this.troubleId = in.readString();
        }

        public static final Creator<PossibleTrouble> CREATOR = new Creator<PossibleTrouble>() {
            @Override
            public PossibleTrouble createFromParcel(Parcel source) {
                return new PossibleTrouble(source);
            }

            @Override
            public PossibleTrouble[] newArray(int size) {
                return new PossibleTrouble[size];
            }
        };
    }

    public static class TroubleData implements Parcelable {
        @SerializedName("trouble_text")
        private String troubleText;
        @SerializedName("trouble_id")
        private String troubleId;

        public String getTroubleText() {
            return troubleText;
        }

        public void setTroubleText(String troubleText) {
            this.troubleText = troubleText;
        }

        public String getTroubleId() {
            return troubleId;
        }

        public void setTroubleId(String troubleId) {
            this.troubleId = troubleId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.troubleText);
            dest.writeString(this.troubleId);
        }

        public TroubleData() {
        }

        protected TroubleData(Parcel in) {
            this.troubleText = in.readString();
            this.troubleId = in.readString();
        }

        public static final Creator<TroubleData> CREATOR = new Creator<TroubleData>() {
            @Override
            public TroubleData createFromParcel(Parcel source) {
                return new TroubleData(source);
            }

            @Override
            public TroubleData[] newArray(int size) {
                return new TroubleData[size];
            }
        };
    }

    public EditResCenterFormData() {
    }

    public static class SolutionData implements Parcelable {
        @SerializedName("max_refund_idr")
        private String maxRefundIdr;
        @SerializedName("solution_text")
        private String solutionText;
        @SerializedName("solution_id")
        private String solutionId;
        @SerializedName("max_refund")
        private Integer maxRefund;
        @SerializedName("refund_text_desc")
        private String refundTextDesc;
        @SerializedName("show_refund_box")
        private Integer showRefundBox;
        @SerializedName("refund_type")
        private Integer refundType;

        public String getMaxRefundIdr() {
            return maxRefundIdr;
        }

        public void setMaxRefundIdr(String maxRefundIdr) {
            this.maxRefundIdr = maxRefundIdr;
        }

        public String getSolutionText() {
            return solutionText;
        }

        public void setSolutionText(String solutionText) {
            this.solutionText = solutionText;
        }

        public String getSolutionId() {
            return solutionId;
        }

        public void setSolutionId(String solutionId) {
            this.solutionId = solutionId;
        }

        public Integer getMaxRefund() {
            return maxRefund;
        }

        public void setMaxRefund(Integer maxRefund) {
            this.maxRefund = maxRefund;
        }

        public String getRefundTextDesc() {
            return refundTextDesc;
        }

        public void setRefundTextDesc(String refundTextDesc) {
            this.refundTextDesc = refundTextDesc;
        }

        public Integer getShowRefundBox() {
            return showRefundBox;
        }

        public void setShowRefundBox(Integer showRefundBox) {
            this.showRefundBox = showRefundBox;
        }

        public Integer getRefundType() {
            return refundType;
        }

        public void setRefundType(Integer refundType) {
            this.refundType = refundType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.maxRefundIdr);
            dest.writeString(this.solutionText);
            dest.writeString(this.solutionId);
            dest.writeValue(this.maxRefund);
            dest.writeString(this.refundTextDesc);
            dest.writeValue(this.showRefundBox);
            dest.writeValue(this.refundType);
        }

        public SolutionData() {
        }

        protected SolutionData(Parcel in) {
            this.maxRefundIdr = in.readString();
            this.solutionText = in.readString();
            this.solutionId = in.readString();
            this.maxRefund = (Integer) in.readValue(Integer.class.getClassLoader());
            this.refundTextDesc = in.readString();
            this.showRefundBox = (Integer) in.readValue(Integer.class.getClassLoader());
            this.refundType = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<SolutionData> CREATOR = new Creator<SolutionData>() {
            @Override
            public SolutionData createFromParcel(Parcel source) {
                return new SolutionData(source);
            }

            @Override
            public SolutionData[] newArray(int size) {
                return new SolutionData[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.form, flags);
        dest.writeTypedList(this.listTs);
        dest.writeTypedList(this.listProd);
        dest.writeTypedList(this.listSolution);
    }

    protected EditResCenterFormData(Parcel in) {
        this.form = in.readParcelable(Form.class.getClassLoader());
        this.listTs = in.createTypedArrayList(TroubleCategoryData.CREATOR);
        this.listProd = in.createTypedArrayList(ProductData.CREATOR);
        this.listSolution = in.createTypedArrayList(SolutionData.CREATOR);
    }

    public static final Creator<EditResCenterFormData> CREATOR = new Creator<EditResCenterFormData>() {
        @Override
        public EditResCenterFormData createFromParcel(Parcel source) {
            return new EditResCenterFormData(source);
        }

        @Override
        public EditResCenterFormData[] newArray(int size) {
            return new EditResCenterFormData[size];
        }
    };
}
