package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationResponse {

    @SerializedName("resConvId")
    private int resConvId;

    @SerializedName("action")
    private ConversationActionResponse action;

    @SerializedName("message")
    private String message;

    @SerializedName("address")
    private ConversationAddressResponse address;

    @SerializedName("shippingDetail")
    private ConversationShippingDetailResponse shippingDetail;

    @SerializedName("createTime")
    private ConversationCreateTimeResponse createTime;

    @SerializedName("attachments")
    private List<ConversationAttachmentResponse> attachment;

    @SerializedName("trouble")
    private ConversationTroubleResponse trouble;

    @SerializedName("solution")
    private ConversationSolutionResponse solution;

    @SerializedName("products")
    private List<ConversationProductResponse> product;

    @SerializedName("button")
    private ConversationButtonResponse button;

    @SerializedName("flag")
    private ConversationFlagResponse flag;

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public ConversationActionResponse getAction() {
        return action;
    }

    public void setAction(ConversationActionResponse action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConversationAddressResponse getAddress() {
        return address;
    }

    public void setAddress(ConversationAddressResponse address) {
        this.address = address;
    }

    public ConversationShippingDetailResponse getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(ConversationShippingDetailResponse shippingDetail) {
        this.shippingDetail = shippingDetail;
    }

    public ConversationCreateTimeResponse getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ConversationCreateTimeResponse createTime) {
        this.createTime = createTime;
    }

    public List<ConversationAttachmentResponse> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<ConversationAttachmentResponse> attachment) {
        this.attachment = attachment;
    }

    public ConversationTroubleResponse getTrouble() {
        return trouble;
    }

    public void setTrouble(ConversationTroubleResponse trouble) {
        this.trouble = trouble;
    }

    public ConversationSolutionResponse getSolution() {
        return solution;
    }

    public void setSolution(ConversationSolutionResponse solution) {
        this.solution = solution;
    }

    public List<ConversationProductResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ConversationProductResponse> product) {
        this.product = product;
    }

    public ConversationButtonResponse getButton() {
        return button;
    }

    public void setButton(ConversationButtonResponse button) {
        this.button = button;
    }

    public ConversationFlagResponse getFlag() {
        return flag;
    }

    public void setFlag(ConversationFlagResponse flag) {
        this.flag = flag;
    }
}
