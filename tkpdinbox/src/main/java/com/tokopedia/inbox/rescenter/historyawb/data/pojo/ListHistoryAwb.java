package com.tokopedia.inbox.rescenter.historyawb.data.pojo;

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
    @SerializedName("createTimeFullStr")
    private String createTimeFullStr;
    @SerializedName("remark")
    private String remark;
    @SerializedName("attachments")
    private List<Attachments> attachments;
    @SerializedName("button")
    private Button button;

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

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
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

    public static class Button {
        @SerializedName("editResi")
        private int editResi;
        @SerializedName("trackResi")
        private int trackResi;

        public int getEditResi() {
            return editResi;
        }

        public void setEditResi(int editResi) {
            this.editResi = editResi;
        }

        public int getTrackResi() {
            return trackResi;
        }

        public void setTrackResi(int trackResi) {
            this.trackResi = trackResi;
        }
    }
}
