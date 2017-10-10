package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationDomain {

    private int resConvId;
    private ConversationActionDomain action;
    private String message;
    private String address;
    private String awbNumber;
    private ConversationCreateTimeDomain createTime;
    private List<ConversationAttachmentDomain> attachment;
    private ConversationTroubleDomain trouble;
    private ConversationSolutionDomain solution;
    private List<ConversationProductDomain> product;
    private ConversationButtonDomain button;
    private ConversationFlagDomain flag;

    public ConversationDomain(int resConvId,
                              ConversationActionDomain action,
                              String message,
                              String address,
                              String awbNumber,
                              ConversationCreateTimeDomain createTime,
                              List<ConversationAttachmentDomain> attachment,
                              ConversationTroubleDomain trouble,
                              ConversationSolutionDomain solution,
                              List<ConversationProductDomain> product,
                              ConversationButtonDomain button,
                              ConversationFlagDomain flag) {
        this.resConvId = resConvId;
        this.action = action;
        this.message = message;
        this.address = address;
        this.awbNumber = awbNumber;
        this.createTime = createTime;
        this.attachment = attachment;
        this.trouble = trouble;
        this.solution = solution;
        this.product = product;
        this.button = button;
        this.flag = flag;
    }

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public ConversationActionDomain getAction() {
        return action;
    }

    public void setAction(ConversationActionDomain action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public ConversationCreateTimeDomain getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ConversationCreateTimeDomain createTime) {
        this.createTime = createTime;
    }

    public List<ConversationAttachmentDomain> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<ConversationAttachmentDomain> attachment) {
        this.attachment = attachment;
    }

    public ConversationTroubleDomain getTrouble() {
        return trouble;
    }

    public void setTrouble(ConversationTroubleDomain trouble) {
        this.trouble = trouble;
    }

    public ConversationSolutionDomain getSolution() {
        return solution;
    }

    public void setSolution(ConversationSolutionDomain solution) {
        this.solution = solution;
    }

    public List<ConversationProductDomain> getProduct() {
        return product;
    }

    public void setProduct(List<ConversationProductDomain> product) {
        this.product = product;
    }

    public ConversationButtonDomain getButton() {
        return button;
    }

    public void setButton(ConversationButtonDomain button) {
        this.button = button;
    }

    public ConversationFlagDomain getFlag() {
        return flag;
    }

    public void setFlag(ConversationFlagDomain flag) {
        this.flag = flag;
    }
}
