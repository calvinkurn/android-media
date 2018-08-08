package com.tokopedia.inbox.rescenter.create.model.responsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 6/20/16.
 */
public class CreateResCenterFormData implements Parcelable {

    @SerializedName("form")
    private FormValueData form;
    @SerializedName("list_ts")
    private List<TroubleCategoryData> listTs;
    @SerializedName("list")
    private List<ProductData> listProd;
    @SerializedName("form_solution")
    private List<SolutionData> listSolution;

    public FormValueData getForm() {
        return form;
    }

    public void setForm(FormValueData form) {
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

    public CreateResCenterFormData() {
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

    protected CreateResCenterFormData(Parcel in) {
        this.form = in.readParcelable(FormValueData.class.getClassLoader());
        this.listTs = in.createTypedArrayList(TroubleCategoryData.CREATOR);
        this.listProd = in.createTypedArrayList(ProductData.CREATOR);
        this.listSolution = in.createTypedArrayList(SolutionData.CREATOR);
    }

    public static final Creator<CreateResCenterFormData> CREATOR = new Creator<CreateResCenterFormData>() {
        @Override
        public CreateResCenterFormData createFromParcel(Parcel source) {
            return new CreateResCenterFormData(source);
        }

        @Override
        public CreateResCenterFormData[] newArray(int size) {
            return new CreateResCenterFormData[size];
        }
    };

    /**
     * Created on 7/5/16.
     */
    public static class SolutionData implements Parcelable {

        @SerializedName("max_refund_idr")
        private String maxRefundIdr;
        @SerializedName("solution_text")
        private String solutionText;
        @SerializedName("solution_id")
        private String solutionId;
        @SerializedName("max_refund")
        private Integer maxRefund;
        @SerializedName("is_show_refund")
        private Integer isShowRefund;
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

        public Integer getIsShowRefund() {
            return isShowRefund;
        }

        public void setIsShowRefund(Integer isShowRefund) {
            this.isShowRefund = isShowRefund;
        }

        public SolutionData() {
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
            dest.writeValue(this.isShowRefund);
            dest.writeString(this.refundTextDesc);
            dest.writeValue(this.showRefundBox);
            dest.writeValue(this.refundType);
        }

        protected SolutionData(Parcel in) {
            this.maxRefundIdr = in.readString();
            this.solutionText = in.readString();
            this.solutionId = in.readString();
            this.maxRefund = (Integer) in.readValue(Integer.class.getClassLoader());
            this.isShowRefund = (Integer) in.readValue(Integer.class.getClassLoader());
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

    /**
     * Created on 7/5/16.
     */
    public static class FormValueData implements Parcelable {

        @SerializedName("order_shipping_fee_idr")
        @Expose
        private String orderShippingFeeIdr;
        @SerializedName("order_shop_url")
        @Expose
        private String orderShopUrl;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("order_open_amount")
        @Expose
        private String orderOpenAmount;
        @SerializedName("order_pdf_url")
        @Expose
        private String orderPdfUrl;
        @SerializedName("order_shipping_fee")
        @Expose
        private String orderShippingFee;
        @SerializedName("order_open_amount_idr")
        @Expose
        private String orderOpenAmountIdr;
        @SerializedName("order_shop_name")
        @Expose
        private String orderShopName;
        @SerializedName("order_is_customer")
        @Expose
        private int orderIsCustomer;
        @SerializedName("order_invoice_ref_num")
        @Expose
        private String orderInvoiceRefNum;
        @SerializedName("order_product_fee")
        @Expose
        private String orderProductFee;
        @SerializedName("order_product_fee_idr")
        @Expose
        private String orderProductFeeIdr;

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

        public String getOrderOpenAmount() {
            return orderOpenAmount;
        }

        public void setOrderOpenAmount(String orderOpenAmount) {
            this.orderOpenAmount = orderOpenAmount;
        }

        public String getOrderPdfUrl() {
            return orderPdfUrl;
        }

        public void setOrderPdfUrl(String orderPdfUrl) {
            this.orderPdfUrl = orderPdfUrl;
        }

        public String getOrderShippingFee() {
            return orderShippingFee;
        }

        public void setOrderShippingFee(String orderShippingFee) {
            this.orderShippingFee = orderShippingFee;
        }

        public String getOrderOpenAmountIdr() {
            return orderOpenAmountIdr;
        }

        public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
            this.orderOpenAmountIdr = orderOpenAmountIdr;
        }

        public String getOrderShopName() {
            return orderShopName;
        }

        public void setOrderShopName(String orderShopName) {
            this.orderShopName = orderShopName;
        }

        public int getOrderIsCustomer() {
            return orderIsCustomer;
        }

        public void setOrderIsCustomer(int orderIsCustomer) {
            this.orderIsCustomer = orderIsCustomer;
        }

        public String getOrderInvoiceRefNum() {
            return orderInvoiceRefNum;
        }

        public void setOrderInvoiceRefNum(String orderInvoiceRefNum) {
            this.orderInvoiceRefNum = orderInvoiceRefNum;
        }

        public String getOrderProductFee() {
            return orderProductFee;
        }

        public void setOrderProductFee(String orderProductFee) {
            this.orderProductFee = orderProductFee;
        }

        public String getOrderProductFeeIdr() {
            return orderProductFeeIdr;
        }

        public void setOrderProductFeeIdr(String orderProductFeeIdr) {
            this.orderProductFeeIdr = orderProductFeeIdr;
        }

        public FormValueData() {
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
            dest.writeString(this.orderOpenAmount);
            dest.writeString(this.orderPdfUrl);
            dest.writeString(this.orderShippingFee);
            dest.writeString(this.orderOpenAmountIdr);
            dest.writeString(this.orderShopName);
            dest.writeInt(this.orderIsCustomer);
            dest.writeString(this.orderInvoiceRefNum);
            dest.writeString(this.orderProductFee);
            dest.writeString(this.orderProductFeeIdr);
        }

        protected FormValueData(Parcel in) {
            this.orderShippingFeeIdr = in.readString();
            this.orderShopUrl = in.readString();
            this.orderId = in.readString();
            this.orderOpenAmount = in.readString();
            this.orderPdfUrl = in.readString();
            this.orderShippingFee = in.readString();
            this.orderOpenAmountIdr = in.readString();
            this.orderShopName = in.readString();
            this.orderIsCustomer = in.readInt();
            this.orderInvoiceRefNum = in.readString();
            this.orderProductFee = in.readString();
            this.orderProductFeeIdr = in.readString();
        }

        public static final Creator<FormValueData> CREATOR = new Creator<FormValueData>() {
            @Override
            public FormValueData createFromParcel(Parcel source) {
                return new FormValueData(source);
            }

            @Override
            public FormValueData[] newArray(int size) {
                return new FormValueData[size];
            }
        };
    }

    /**
     * Created on 7/28/16.
     */
    public static class ProductData implements Parcelable {


        @SerializedName("pt_is_free_return")
        private int isFreeReturn;
        @SerializedName("pt_primary_dtl_photo")
        private String primaryDtlPhoto;
        @SerializedName("pt_product_name")
        private String productName;
        @SerializedName("pt_primary_photo")
        private String primaryPhoto;
        @SerializedName("pt_show_input_quantity")
        private int showInputQuantity;
        @SerializedName("pt_product_id")
        private String productId;
        @SerializedName("pt_order_dtl_id")
        private String orderDetailId;
        @SerializedName("pt_quantity")
        private int quantity;

        public int getIsFreeReturn() {
            return isFreeReturn;
        }

        public void setIsFreeReturn(int isFreeReturn) {
            this.isFreeReturn = isFreeReturn;
        }

        public String getPrimaryDtlPhoto() {
            return primaryDtlPhoto;
        }

        public void setPrimaryDtlPhoto(String primaryDtlPhoto) {
            this.primaryDtlPhoto = primaryDtlPhoto;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPrimaryPhoto() {
            return primaryPhoto;
        }

        public void setPrimaryPhoto(String primaryPhoto) {
            this.primaryPhoto = primaryPhoto;
        }

        public int getShowInputQuantity() {
            return showInputQuantity;
        }

        public void setShowInputQuantity(int showInputQuantity) {
            this.showInputQuantity = showInputQuantity;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOrderDetailId() {
            return orderDetailId;
        }

        public void setOrderDetailId(String orderDetailId) {
            this.orderDetailId = orderDetailId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.isFreeReturn);
            dest.writeString(this.primaryDtlPhoto);
            dest.writeString(this.productName);
            dest.writeString(this.primaryPhoto);
            dest.writeInt(this.showInputQuantity);
            dest.writeString(this.productId);
            dest.writeString(this.orderDetailId);
            dest.writeInt(this.quantity);
        }

        public ProductData() {
        }

        protected ProductData(Parcel in) {
            this.isFreeReturn = in.readInt();
            this.primaryDtlPhoto = in.readString();
            this.productName = in.readString();
            this.primaryPhoto = in.readString();
            this.showInputQuantity = in.readInt();
            this.productId = in.readString();
            this.orderDetailId = in.readString();
            this.quantity = in.readInt();
        }

        public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
            @Override
            public ProductData createFromParcel(Parcel source) {
                return new ProductData(source);
            }

            @Override
            public ProductData[] newArray(int size) {
                return new ProductData[size];
            }
        };
    }

    /**
     * Created on 7/5/16.
     */
    public static class TroubleCategoryData implements Parcelable {

        @SerializedName("category_trouble_id")
        @Expose
        private String categoryTroubleId;
        @SerializedName("category_trouble_text")
        @Expose
        private String categoryTroubleText;
        @SerializedName("trouble_list")
        @Expose
        private List<TroubleData> troubleList;
        @SerializedName("trouble_list_fr")
        @Expose
        private List<TroubleData> troubleListFreeReturn;
        @SerializedName("attachment")
        @Expose
        private Integer attachment;
        @SerializedName("product_is_received")
        @Expose
        private Integer productIsReceived;
        @SerializedName("product_related")
        @Expose
        private Integer productRelated;

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
            dest.writeString(this.categoryTroubleId);
            dest.writeString(this.categoryTroubleText);
            dest.writeTypedList(this.troubleList);
            dest.writeTypedList(this.troubleListFreeReturn);
            dest.writeValue(this.attachment);
            dest.writeValue(this.productIsReceived);
            dest.writeValue(this.productRelated);
        }

        protected TroubleCategoryData(Parcel in) {
            this.categoryTroubleId = in.readString();
            this.categoryTroubleText = in.readString();
            this.troubleList = in.createTypedArrayList(TroubleData.CREATOR);
            this.troubleListFreeReturn = in.createTypedArrayList(TroubleData.CREATOR);
            this.attachment = (Integer) in.readValue(Integer.class.getClassLoader());
            this.productIsReceived = (Integer) in.readValue(Integer.class.getClassLoader());
            this.productRelated = (Integer) in.readValue(Integer.class.getClassLoader());
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

    /**
     * Created on 7/5/16.
     */
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
}
