package com.tokopedia.inbox.rescenter.discussion.domain.model;

import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class ActionDiscussionModel {

    private String cacheKey;
    private String token;
    private DiscussionItemViewModel replyDiscussionData;
    private String message;
    private String resolutionId;
    private int flagReceived;
    private List<AttachmentViewModel> attachment;
    private boolean hasAttachment;
    private String serverId;
    private String uploadHost;
    private List<UploadImageModel> uploadedFile;

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setReplyDiscussionData(DiscussionItemViewModel replyDiscussionData) {
        this.replyDiscussionData = replyDiscussionData;
    }

    public DiscussionItemViewModel getReplyDiscussionData() {
        return replyDiscussionData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public int getFlagReceived() {
        return flagReceived;
    }

    public void setFlagReceived(int flagReceived) {
        this.flagReceived = flagReceived;
    }

    public void setAttachment(List<AttachmentViewModel> attachment) {
        this.attachment = attachment;
    }

    public List<AttachmentViewModel> getAttachment() {
        return attachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadedFile(List<UploadImageModel> uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<UploadImageModel> getUploadedFile() {
        return uploadedFile;
    }
}
