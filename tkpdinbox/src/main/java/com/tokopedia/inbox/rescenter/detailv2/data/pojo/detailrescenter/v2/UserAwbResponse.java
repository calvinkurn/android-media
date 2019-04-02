package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class UserAwbResponse {

    @SerializedName("resConvId")
    private int resConvId;
    @SerializedName("awb")
    private String awb;
    @SerializedName("shipping")
    private ShippingResponse shipping;
    @SerializedName("by")
    private ByResponse by;
    @SerializedName("trackable")
    private int trackable;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("createTimeFullStr")
    private String createTimeFullStr;
    @SerializedName("attachments")
    private List<AttachmentResponse> attachments;

    public List<AttachmentResponse> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public ShippingResponse getShipping() {
        return shipping;
    }

    public void setShipping(ShippingResponse shipping) {
        this.shipping = shipping;
    }

    public int getTrackable() {
        return trackable;
    }

    public void setTrackable(int trackable) {
        this.trackable = trackable;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public ByResponse getBy() {
        return by;
    }

    public void setBy(ByResponse by) {
        this.by = by;
    }

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
    }
}
