package com.tokopedia.inbox.rescenter.edit.model.responsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.database.repository.ResCenterAttachmentRepository;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.ActionResponseData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import java.util.List;

/**
 * Created on 6/16/16.
 */
public class ActionParameterPassData implements Parcelable {

    // collected in step 1
    private String resolutionID;
    private EditResCenterFormData formData;
    private DetailResCenterData detailData;
    private EditResCenterFormData.TroubleCategoryData troubleCategoryChoosen;
    private int packageStatus;

    // collected in step 2
    private EditResCenterFormData.TroubleData troubleChoosen;
    private List<PassProductTrouble> productTroubleChoosenList;
    private String inputDescription;

    // collected in step 3
    private EditResCenterFormData.SolutionData solutionChoosen;
    private String refund;
    private List<ResCenterAttachment> attachmentData;

    // collected in intent service
    private String serverID;
    private String uploadHost;
    private String attachmentString;
    private String postKey;
    private boolean byPassFlag;
    private String fileUploaded;
    private ActionResponseData actionResponseData;
    private String replyMsg;
    private AppealResCenterFormData appealFormData;

    public DetailResCenterData getDetailData() {
        return detailData;
    }

    public void setDetailData(DetailResCenterData detailData) {
        this.detailData = detailData;
    }

    public List<PassProductTrouble> getProductTroubleChoosenList() {
        return productTroubleChoosenList;
    }

    public void setProductTroubleChoosenList(List<PassProductTrouble> productTroubleChoosenList) {
        this.productTroubleChoosenList = productTroubleChoosenList;
    }

    public String getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public List<ResCenterAttachment> getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(List<ResCenterAttachment> attachmentData) {
        this.attachmentData = attachmentData;
    }

    public EditResCenterFormData getFormData() {
        return formData;
    }

    public void setFormData(EditResCenterFormData formData) {
        this.formData = formData;
    }

    public EditResCenterFormData.TroubleCategoryData getTroubleCategoryChoosen() {
        return troubleCategoryChoosen;
    }

    public void setTroubleCategoryChoosen(EditResCenterFormData.TroubleCategoryData troubleCategoryChoosen) {
        this.troubleCategoryChoosen = troubleCategoryChoosen;
    }

    public EditResCenterFormData.TroubleData getTroubleChoosen() {
        return troubleChoosen;
    }

    public void setTroubleChoosen(EditResCenterFormData.TroubleData troubleChoosen) {
        this.troubleChoosen = troubleChoosen;
    }

    public EditResCenterFormData.SolutionData getSolutionChoosen() {
        return solutionChoosen;
    }

    public void setSolutionChoosen(EditResCenterFormData.SolutionData solutionChoosen) {
        this.solutionChoosen = solutionChoosen;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public ActionParameterPassData() {
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getAttachmentString() {
        return attachmentString;
    }

    public void setAttachmentString(String attachmentString) {
        this.attachmentString = attachmentString;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setByPassFlag(boolean byPassFlag) {
        this.byPassFlag = byPassFlag;
    }

    public boolean isByPassFlag() {
        return byPassFlag;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setActionResponseData(ActionResponseData actionResponseData) {
        this.actionResponseData = actionResponseData;
    }

    public ActionResponseData getActionResponseData() {
        return actionResponseData;
    }

    public void setPackageStatus(int packageStatus) {
        this.packageStatus = packageStatus;
    }

    public int getPackageStatus() {
        return packageStatus;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public void setAppealFormData(AppealResCenterFormData appealFormData) {
        this.appealFormData = appealFormData;
    }

    public AppealResCenterFormData getAppealFormData() {
        return appealFormData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resolutionID);
        dest.writeParcelable(this.formData, flags);
        dest.writeParcelable(this.detailData, flags);
        dest.writeParcelable(this.troubleCategoryChoosen, flags);
        dest.writeInt(this.packageStatus);
        dest.writeParcelable(this.troubleChoosen, flags);
        dest.writeTypedList(this.productTroubleChoosenList);
        dest.writeString(this.inputDescription);
        dest.writeParcelable(this.solutionChoosen, flags);
        dest.writeString(this.refund);
        dest.writeTypedList(this.attachmentData);
        dest.writeString(this.serverID);
        dest.writeString(this.uploadHost);
        dest.writeString(this.attachmentString);
        dest.writeString(this.postKey);
        dest.writeByte(this.byPassFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.fileUploaded);
        dest.writeParcelable(this.actionResponseData, flags);
        dest.writeString(this.replyMsg);
        dest.writeParcelable(this.appealFormData, flags);
    }

    protected ActionParameterPassData(Parcel in) {
        this.resolutionID = in.readString();
        this.formData = in.readParcelable(EditResCenterFormData.class.getClassLoader());
        this.detailData = in.readParcelable(DetailResCenterData.class.getClassLoader());
        this.troubleCategoryChoosen = in.readParcelable(EditResCenterFormData.TroubleCategoryData.class.getClassLoader());
        this.packageStatus = in.readInt();
        this.troubleChoosen = in.readParcelable(EditResCenterFormData.TroubleData.class.getClassLoader());
        this.productTroubleChoosenList = in.createTypedArrayList(PassProductTrouble.CREATOR);
        this.inputDescription = in.readString();
        this.solutionChoosen = in.readParcelable(EditResCenterFormData.SolutionData.class.getClassLoader());
        this.refund = in.readString();
        this.attachmentData = in.createTypedArrayList(ResCenterAttachmentRepository.CREATOR);
        this.serverID = in.readString();
        this.uploadHost = in.readString();
        this.attachmentString = in.readString();
        this.postKey = in.readString();
        this.byPassFlag = in.readByte() != 0;
        this.fileUploaded = in.readString();
        this.actionResponseData = in.readParcelable(ActionResponseData.class.getClassLoader());
        this.replyMsg = in.readString();
        this.appealFormData = in.readParcelable(AppealResCenterFormData.class.getClassLoader());
    }

    public static final Creator<ActionParameterPassData> CREATOR = new Creator<ActionParameterPassData>() {
        @Override
        public ActionParameterPassData createFromParcel(Parcel source) {
            return new ActionParameterPassData(source);
        }

        @Override
        public ActionParameterPassData[] newArray(int size) {
            return new ActionParameterPassData[size];
        }
    };
}
