package com.tokopedia.transaction.purchase.model.response.txverification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 25/05/2016.
 */
public class TxVerData implements Parcelable {
    private static final String TAG = TxVerData.class.getSimpleName();

    @SerializedName("img_proof_url")
    @Expose
    private String imgProofUrl;
    @SerializedName("order_count")
    @Expose
    private String orderCount;
    @SerializedName("user_account_name")
    @Expose
    private String userAccountName;
    @SerializedName("user_bank_name")
    @Expose
    private String userBankName;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
    @SerializedName("img_proof")
    @Expose
    private ImgProof imgProof;
    @SerializedName("payment_ref_num")
    @Expose
    private String paymentRefNum;
    @SerializedName("user_account_no")
    @Expose
    private String userAccountNo;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("system_account_no")
    @Expose
    private String systemAccountNo;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("button")
    @Expose
    private Button button;
    @SerializedName("has_user_bank")
    @Expose
    private Integer hasUserBank;
    @SerializedName("payment_amount")
    @Expose
    private String paymentAmount;
    @SerializedName("howtopay_flag")
    @Expose
    private Integer howtopay;
    @SerializedName("howtopay_url")
    @Expose
    private String howtopayUrl;

    public String getImgProofUrl() {
        return imgProofUrl;
    }

    public void setImgProofUrl(String imgProofUrl) {
        this.imgProofUrl = imgProofUrl;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public String getUserBankName() {
        return userBankName;
    }

    public void setUserBankName(String userBankName) {
        this.userBankName = userBankName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ImgProof getImgProof() {
        return imgProof;
    }

    public void setImgProof(ImgProof imgProof) {
        this.imgProof = imgProof;
    }

    public String getPaymentRefNum() {
        return paymentRefNum;
    }

    public void setPaymentRefNum(String paymentRefNum) {
        this.paymentRefNum = paymentRefNum;
    }

    public String getUserAccountNo() {
        return userAccountNo;
    }

    public void setUserAccountNo(String userAccountNo) {
        this.userAccountNo = userAccountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSystemAccountNo() {
        return systemAccountNo;
    }

    public void setSystemAccountNo(String systemAccountNo) {
        this.systemAccountNo = systemAccountNo;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Integer getHasUserBank() {
        return hasUserBank;
    }

    public void setHasUserBank(Integer hasUserBank) {
        this.hasUserBank = hasUserBank;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getHowtopay() {
        return howtopay;
    }

    public void setHowtopay(Integer howtopay) {
        this.howtopay = howtopay;
    }

    public String getHowtopayUrl() {
        return howtopayUrl;
    }

    public void setHowtopayUrl(String howtopayUrl) {
        this.howtopayUrl = howtopayUrl;
    }

    public static Creator<TxVerData> getCREATOR() {
        return CREATOR;
    }

    protected TxVerData(Parcel in) {
        imgProofUrl = in.readString();
        orderCount = in.readString();
        userAccountName = in.readString();
        userBankName = in.readString();
        paymentDate = in.readString();
        imgProof = (ImgProof) in.readValue(ImgProof.class.getClassLoader());
        paymentRefNum = in.readString();
        userAccountNo = in.readString();
        bankName = in.readString();
        systemAccountNo = in.readString();
        paymentId = in.readString();
        button = (Button) in.readValue(Button.class.getClassLoader());
        hasUserBank = in.readByte() == 0x00 ? null : in.readInt();
        paymentAmount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgProofUrl);
        dest.writeString(orderCount);
        dest.writeString(userAccountName);
        dest.writeString(userBankName);
        dest.writeString(paymentDate);
        dest.writeValue(imgProof);
        dest.writeString(paymentRefNum);
        dest.writeString(userAccountNo);
        dest.writeString(bankName);
        dest.writeString(systemAccountNo);
        dest.writeString(paymentId);
        dest.writeValue(button);
        if (hasUserBank == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hasUserBank);
        }
        dest.writeString(paymentAmount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxVerData> CREATOR = new Parcelable.Creator<TxVerData>() {
        @Override
        public TxVerData createFromParcel(Parcel in) {
            return new TxVerData(in);
        }

        @Override
        public TxVerData[] newArray(int size) {
            return new TxVerData[size];
        }
    };
}
