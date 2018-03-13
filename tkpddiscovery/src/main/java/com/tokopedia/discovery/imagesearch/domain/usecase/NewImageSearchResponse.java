
package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Parcel;

import com.aliyuncs.transform.UnmarshallerContext;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class NewImageSearchResponse extends com.aliyuncs.AcsResponse implements Serializable {

    @SerializedName("Auctions")
    @Expose
    private Auctions auctions;
    @SerializedName("Head")
    @Expose
    private Head head;
    @SerializedName("PicInfo")
    @Expose
    private PicInfo picInfo;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RequestId")
    @Expose
    private String requestId;
    @SerializedName("Success")
    @Expose
    private boolean success;
    @SerializedName("Code")
    @Expose
    private int code;

    private ArrayList<Auction> auctionsArrayList;

    private final static long serialVersionUID = -860839202756349796L;

    protected NewImageSearchResponse(Parcel in) {
        this.auctions = ((Auctions) in.readValue((Auctions.class.getClassLoader())));
        this.head = ((Head) in.readValue((Head.class.getClassLoader())));
        this.picInfo = ((PicInfo) in.readValue((PicInfo.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.requestId = ((String) in.readValue((String.class.getClassLoader())));
        this.success = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.code = ((int) in.readValue((int.class.getClassLoader())));
        this.auctionsArrayList = (ArrayList<Auction>) in.readValue(Auctions.class.getClassLoader());
    }

    /**
     * No args constructor for use in serialization
     */
    public NewImageSearchResponse() {
    }

    public NewImageSearchResponse getInstance(UnmarshallerContext context) {
        return NewImageSearchResponseUnmarshaller.unmarshall(this, context);
    }

    /**
     * @param message
     * @param auctions
     * @param requestId
     * @param code
     * @param head
     * @param success
     * @param picInfo
     */
    public NewImageSearchResponse(Auctions auctions, Head head, PicInfo picInfo, String message,
                                  String requestId, boolean success, int code, ArrayList<Auction> auctionsArrayList) {
        super();
        this.auctions = auctions;
        this.head = head;
        this.picInfo = picInfo;
        this.message = message;
        this.requestId = requestId;
        this.success = success;
        this.code = code;
        this.auctionsArrayList = auctionsArrayList;
    }

    public Auctions getAuctions() {
        return auctions;
    }

    public void setAuctions(Auctions auctions) {
        this.auctions = auctions;
    }

    public NewImageSearchResponse withAuctions(Auctions auctions) {
        this.auctions = auctions;
        return this;
    }

    public ArrayList<Auction> getAuctionsArrayList() {
        return auctionsArrayList;
    }

    public void setAuctionsArrayList(ArrayList<Auction> auctionsArrayList) {
        this.auctionsArrayList = auctionsArrayList;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public NewImageSearchResponse withHead(Head head) {
        this.head = head;
        return this;
    }

    public PicInfo getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(PicInfo picInfo) {
        this.picInfo = picInfo;
    }

    public NewImageSearchResponse withPicInfo(PicInfo picInfo) {
        this.picInfo = picInfo;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NewImageSearchResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public NewImageSearchResponse withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public NewImageSearchResponse withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public NewImageSearchResponse withCode(int code) {
        this.code = code;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(auctions);
        dest.writeValue(head);
        dest.writeValue(picInfo);
        dest.writeValue(message);
        dest.writeValue(requestId);
        dest.writeValue(success);
        dest.writeValue(code);
        dest.writeValue(auctionsArrayList);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("auctions", auctions).append("head", head).append("picInfo", picInfo).append("message", message).append("requestId", requestId).append("success", success).append("code", code).toString();
    }
}
