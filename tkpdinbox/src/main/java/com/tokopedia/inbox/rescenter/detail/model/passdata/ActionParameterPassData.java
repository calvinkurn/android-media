package com.tokopedia.inbox.rescenter.detail.model.passdata;

import com.tokopedia.core.database.model.AttachmentResCenterDB;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.ResCenterActionData;

import java.util.List;

/**
 * Created by hangnadi on 3/2/16.
 */
public class ActionParameterPassData {

    private String resolutionID;
    private String hasSolution;
    private String startUt;
    private String lastUt;
    private String ediSolFlag;
    private String flagReceived;
    private String photos;
    private String refundAmount;
    private String replyMsg;
    private String solutionState;
    private String solutionName;
    private String troubleState;
    private String troubleName;
    private String fileUploaded;
    private String postKey;
    private String serverLanguage;
    private String serverID;
    private String uploadHost;
    private String attachmentString;
    private String filePath;
    private ResCenterActionData resCenterActionData;
    private Boolean byPassFlag;
    private List<AttachmentResCenterDB> listImages;
    private String conversationID;
    private String shipmentID;
    private String shippingRefNum;

    public String getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public String getHasSolution() {
        return hasSolution;
    }

    public void setHasSolution(String hasSolution) {
        this.hasSolution = hasSolution;
    }

    public String getStartUt() {
        return startUt;
    }

    public void setStartUt(String startUt) {
        this.startUt = startUt;
    }

    public String getLastUt() {
        return lastUt;
    }

    public void setLastUt(String lastUt) {
        this.lastUt = lastUt;
    }

    public String getEdiSolFlag() {
        return ediSolFlag;
    }

    public void setEdiSolFlag(String ediSolFlag) {
        this.ediSolFlag = ediSolFlag;
    }

    public String getFlagReceived() {
        return flagReceived;
    }

    public void setFlagReceived(String flagReceived) {
        this.flagReceived = flagReceived;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getSolutionState() {
        return solutionState;
    }

    public void setSolutionState(String solutionState) {
        this.solutionState = solutionState;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getTroubleState() {
        return troubleState;
    }

    public void setTroubleState(String troubleState) {
        this.troubleState = troubleState;
    }

    public String getTroubleName() {
        return troubleName;
    }

    public void setTroubleName(String troubleName) {
        this.troubleName = troubleName;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getServerLanguage() {
        return serverLanguage;
    }

    public void setServerLanguage(String serverLanguage) {
        this.serverLanguage = serverLanguage;
    }

    public void setServerID(String serverId) {
        this.serverID = serverId;
    }

    public String getServerID() {
        return serverID;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setAttachmentString(String attachmentString) {
        this.attachmentString = attachmentString;
    }

    public String getAttachmentString() {
        return attachmentString;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }


    public void setResCenterActionData(ResCenterActionData resCenterActionData) {
        this.resCenterActionData = resCenterActionData;
    }

    public ResCenterActionData getResCenterActionData() {
        return resCenterActionData;
    }

    public void setByPassFlag(boolean byPassFlag) {
        this.byPassFlag = byPassFlag;
    }

    public Boolean getByPassFlag() {
        return byPassFlag;
    }

    public void setListImages(List<AttachmentResCenterDB> listImages) {
        this.listImages = listImages;
    }

    public List<AttachmentResCenterDB> getListImages() {
        return listImages;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
    }
}
