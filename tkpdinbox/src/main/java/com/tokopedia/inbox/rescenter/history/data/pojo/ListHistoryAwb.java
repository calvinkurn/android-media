package com.tokopedia.inbox.rescenter.history.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/24/17.
 */

public class ListHistoryAwb {

    @SerializedName("resConvId")
    private String resConvId;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("actionByStr")
    private String actionByStr;
    @SerializedName("shippingId")
    private String shippingId;
    @SerializedName("shippingRefNum")
    private String shippingRefNum;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("remark")
    private String remark;
    @SerializedName("attachments")
    private List<ListHistoryAwb.Attachments> attachments;

    public String getResConvId() {
        return resConvId;
    }

    public void setResConvId(String resConvId) {
        this.resConvId = resConvId;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionByStr() {
        return actionByStr;
    }

    public void setActionByStr(String actionByStr) {
        this.actionByStr = actionByStr;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ListHistoryAwb.Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ListHistoryAwb.Attachments> attachments) {
        this.attachments = attachments;
    }

    public static class Attachments {
        @SerializedName("imageThumb")
        private String imageThumb;
        @SerializedName("url")
        private String url;

        public String getImageThumb() {
            return imageThumb;
        }

        public void setImageThumb(String imageThumb) {
            this.imageThumb = imageThumb;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
