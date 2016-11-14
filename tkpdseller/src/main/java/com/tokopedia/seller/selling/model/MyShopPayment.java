
package com.tokopedia.seller.selling.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(parcelsIndex = false)
public class MyShopPayment {

    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("config")
    @Expose
    String config;
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The config
     */
    public String getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     *     The config
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     * 
     * @param serverProcessTime
     *     The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    @Parcel(parcelsIndex = false)
    public static class Data {

        @SerializedName("loc")
        @Expose
        Loc loc;
        @SerializedName("shop_id")
        @Expose
        Integer shopId;
        @SerializedName("shop_payment")
        @Expose
        List<ShopPayment> shopPayment = new ArrayList<ShopPayment>();
        @SerializedName("note")
        @Expose
        List<String> note = new ArrayList<String>();
        @SerializedName("payment_options")
        @Expose
        List<PaymentOption> paymentOptions = new ArrayList<PaymentOption>();

        /**
         *
         * @return
         *     The loc
         */
        public Loc getLoc() {
            return loc;
        }

        /**
         *
         * @param loc
         *     The loc
         */
        public void setLoc(Loc loc) {
            this.loc = loc;
        }

        /**
         *
         * @return
         *     The shopId
         */
        public Integer getShopId() {
            return shopId;
        }

        /**
         *
         * @param shopId
         *     The shop_id
         */
        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        /**
         *
         * @return
         *     The shopPayment
         */
        public List<ShopPayment> getShopPayment() {
            return shopPayment;
        }

        /**
         *
         * @param shopPayment
         *     The shop_payment
         */
        public void setShopPayment(List<ShopPayment> shopPayment) {
            this.shopPayment = shopPayment;
        }

        /**
         *
         * @return
         *     The note
         */
        public List<String> getNote() {
            return note;
        }

        /**
         *
         * @param note
         *     The note
         */
        public void setNote(List<String> note) {
            this.note = note;
        }

        /**
         *
         * @return
         *     The paymentOptions
         */
        public List<PaymentOption> getPaymentOptions() {
            return paymentOptions;
        }

        /**
         *
         * @param paymentOptions
         *     The payment_options
         */
        public void setPaymentOptions(List<PaymentOption> paymentOptions) {
            this.paymentOptions = paymentOptions;
        }

    }

    @Parcel(parcelsIndex = false)
    public static class Loc {

        @SerializedName("6")
        @Expose
        String _6;
        @SerializedName("11")
        @Expose
        String _11;
        @SerializedName("7")
        @Expose
        String _7;
        @SerializedName("9")
        @Expose
        String _9;
        @SerializedName("12")
        @Expose
        String _12;
        @SerializedName("14")
        @Expose
        String _14;
        @SerializedName("8")
        @Expose
        String _8;
        @SerializedName("1")
        @Expose
        String _1;
        @SerializedName("4")
        @Expose
        String _4;
        @SerializedName("13")
        @Expose
        String _13;
        @SerializedName("10")
        @Expose
        String _10;
        @SerializedName("15")
        @Expose
        String _15;

        /**
         *
         * @return
         *     The _6
         */
        public String get6() {
            return _6;
        }

        /**
         *
         * @param _6
         *     The 6
         */
        public void set6(String _6) {
            this._6 = _6;
        }

        /**
         *
         * @return
         *     The _11
         */
        public String get11() {
            return _11;
        }

        /**
         *
         * @param _11
         *     The 11
         */
        public void set11(String _11) {
            this._11 = _11;
        }

        /**
         *
         * @return
         *     The _7
         */
        public String get7() {
            return _7;
        }

        /**
         *
         * @param _7
         *     The 7
         */
        public void set7(String _7) {
            this._7 = _7;
        }

        /**
         *
         * @return
         *     The _9
         */
        public String get9() {
            return _9;
        }

        /**
         *
         * @param _9
         *     The 9
         */
        public void set9(String _9) {
            this._9 = _9;
        }

        /**
         *
         * @return
         *     The _12
         */
        public String get12() {
            return _12;
        }

        /**
         *
         * @param _12
         *     The 12
         */
        public void set12(String _12) {
            this._12 = _12;
        }

        /**
         *
         * @return
         *     The _14
         */
        public String get14() {
            return _14;
        }

        /**
         *
         * @param _14
         *     The 14
         */
        public void set14(String _14) {
            this._14 = _14;
        }

        /**
         *
         * @return
         *     The _8
         */
        public String get8() {
            return _8;
        }

        /**
         *
         * @param _8
         *     The 8
         */
        public void set8(String _8) {
            this._8 = _8;
        }

        /**
         *
         * @return
         *     The _1
         */
        public String get1() {
            return _1;
        }

        /**
         *
         * @param _1
         *     The 1
         */
        public void set1(String _1) {
            this._1 = _1;
        }

        /**
         *
         * @return
         *     The _4
         */
        public String get4() {
            return _4;
        }

        /**
         *
         * @param _4
         *     The 4
         */
        public void set4(String _4) {
            this._4 = _4;
        }

        /**
         *
         * @return
         *     The _13
         */
        public String get13() {
            return _13;
        }

        /**
         *
         * @param _13
         *     The 13
         */
        public void set13(String _13) {
            this._13 = _13;
        }

        /**
         *
         * @return
         *     The _10
         */
        public String get10() {
            return _10;
        }

        /**
         *
         * @param _10
         *     The 10
         */
        public void set10(String _10) {
            this._10 = _10;
        }

        /**
         *
         * @return
         *     The _15
         */
        public String get15() {
            return _15;
        }

        /**
         *
         * @param _15
         *     The 15
         */
        public void set15(String _15) {
            this._15 = _15;
        }


    }
    
    @Parcel(parcelsIndex = false)
    public static class PaymentOption {

        @SerializedName("payment_image")
        @Expose
        String paymentImage;
        @SerializedName("payment_id")
        @Expose
        String paymentId;
        @SerializedName("payment_name")
        @Expose
        String paymentName;
        @SerializedName("payment_info")
        @Expose
        String paymentInfo;
        @SerializedName("payment_default_status")
        @Expose
        String paymentDefaultStatus;

        /**
         *
         * @return
         *     The paymentImage
         */
        public String getPaymentImage() {
            return paymentImage;
        }

        /**
         *
         * @param paymentImage
         *     The payment_image
         */
        public void setPaymentImage(String paymentImage) {
            this.paymentImage = paymentImage;
        }

        /**
         *
         * @return
         *     The paymentId
         */
        public String getPaymentId() {
            return paymentId;
        }

        /**
         *
         * @param paymentId
         *     The payment_id
         */
        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        /**
         *
         * @return
         *     The paymentName
         */
        public String getPaymentName() {
            return paymentName;
        }

        /**
         *
         * @param paymentName
         *     The payment_name
         */
        public void setPaymentName(String paymentName) {
            this.paymentName = paymentName;
        }

        /**
         *
         * @return
         *     The paymentInfo
         */
        public String getPaymentInfo() {
            return paymentInfo;
        }

        /**
         *
         * @param paymentInfo
         *     The payment_info
         */
        public void setPaymentInfo(String paymentInfo) {
            this.paymentInfo = paymentInfo;
        }

        /**
         *
         * @return
         *     The paymentDefaultStatus
         */
        public String getPaymentDefaultStatus() {
            return paymentDefaultStatus;
        }

        /**
         *
         * @param paymentDefaultStatus
         *     The payment_default_status
         */
        public void setPaymentDefaultStatus(String paymentDefaultStatus) {
            this.paymentDefaultStatus = paymentDefaultStatus;
        }

    }

    @Parcel(parcelsIndex = false)
    public static class ShopPayment {

        @SerializedName("payment_image")
        @Expose
        String paymentImage;
        @SerializedName("payment_id")
        @Expose
        String paymentId;
        @SerializedName("payment_name")
        @Expose
        String paymentName;
        @SerializedName("payment_info")
        @Expose
        Integer paymentInfo;
        @SerializedName("payment_default_status")
        @Expose
        String paymentDefaultStatus;

        /**
         *
         * @return
         *     The paymentImage
         */
        public String getPaymentImage() {
            return paymentImage;
        }

        /**
         *
         * @param paymentImage
         *     The payment_image
         */
        public void setPaymentImage(String paymentImage) {
            this.paymentImage = paymentImage;
        }

        /**
         *
         * @return
         *     The paymentId
         */
        public String getPaymentId() {
            return paymentId;
        }

        /**
         *
         * @param paymentId
         *     The payment_id
         */
        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        /**
         *
         * @return
         *     The paymentName
         */
        public String getPaymentName() {
            return paymentName;
        }

        /**
         *
         * @param paymentName
         *     The payment_name
         */
        public void setPaymentName(String paymentName) {
            this.paymentName = paymentName;
        }

        /**
         *
         * @return
         *     The paymentInfo
         */
        public Integer getPaymentInfo() {
            return paymentInfo;
        }

        /**
         *
         * @param paymentInfo
         *     The payment_info
         */
        public void setPaymentInfo(Integer paymentInfo) {
            this.paymentInfo = paymentInfo;
        }

        /**
         *
         * @return
         *     The paymentDefaultStatus
         */
        public String getPaymentDefaultStatus() {
            return paymentDefaultStatus;
        }

        /**
         *
         * @param paymentDefaultStatus
         *     The payment_default_status
         */
        public void setPaymentDefaultStatus(String paymentDefaultStatus) {
            this.paymentDefaultStatus = paymentDefaultStatus;
        }

    }
}
