package com.tokopedia.inbox.rescenter.create.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.inbox.rescenter.create.model.responsedata.ActionResponseData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.List;

/**
 * Created on 6/16/16.
 */
public class ActionParameterPassData implements Parcelable {

    // collected in step 1
    private String orderID;
    private int flagReceived;
    private int troubleID;
    private int solutionID;
    private CreateResCenterFormData formData;
    private CreateResCenterFormData.TroubleCategoryData troubleCategoryChoosen;

    // collected in step 2
    private CreateResCenterFormData.TroubleData troubleChoosen;
    private List<PassProductTrouble> productTroubleChoosenList;
    private String inputDescription;

    // collected in step 3
    private CreateResCenterFormData.SolutionData solutionChoosen;
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

    public int getFlagReceived() {
        return flagReceived;
    }

    public void setFlagReceived(int flagReceived) {
        this.flagReceived = flagReceived;
    }

    public int getTroubleID() {
        return troubleID;
    }

    public void setTroubleID(int troubleID) {
        this.troubleID = troubleID;
    }

    public int getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(int solutionID) {
        this.solutionID = solutionID;
    }

    public List<PassProductTrouble> getProductTroubleChoosenList() {
        return productTroubleChoosenList;
    }

    public void setProductTroubleChoosenList(List<PassProductTrouble> productTroubleChoosenList) {
        this.productTroubleChoosenList = productTroubleChoosenList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<ResCenterAttachment> getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(List<ResCenterAttachment> attachmentData) {
        this.attachmentData = attachmentData;
    }

    public CreateResCenterFormData getFormData() {
        return formData;
    }

    public void setFormData(CreateResCenterFormData formData) {
        this.formData = formData;
    }

    public CreateResCenterFormData.TroubleCategoryData getTroubleCategoryChoosen() {
        return troubleCategoryChoosen;
    }

    public void setTroubleCategoryChoosen(CreateResCenterFormData.TroubleCategoryData troubleCategoryChoosen) {
        this.troubleCategoryChoosen = troubleCategoryChoosen;
    }

    public CreateResCenterFormData.TroubleData getTroubleChoosen() {
        return troubleChoosen;
    }

    public void setTroubleChoosen(CreateResCenterFormData.TroubleData troubleChoosen) {
        this.troubleChoosen = troubleChoosen;
    }

    public CreateResCenterFormData.SolutionData getSolutionChoosen() {
        return solutionChoosen;
    }

    public void setSolutionChoosen(CreateResCenterFormData.SolutionData solutionChoosen) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeInt(this.flagReceived);
        dest.writeInt(this.troubleID);
        dest.writeInt(this.solutionID);
        dest.writeParcelable(this.formData, flags);
        dest.writeParcelable(this.troubleCategoryChoosen, flags);
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
    }

    protected ActionParameterPassData(Parcel in) {
        this.orderID = in.readString();
        this.flagReceived = in.readInt();
        this.troubleID = in.readInt();
        this.solutionID = in.readInt();
        this.formData = in.readParcelable(CreateResCenterFormData.class.getClassLoader());
        this.troubleCategoryChoosen = in.readParcelable(CreateResCenterFormData.TroubleCategoryData.class.getClassLoader());
        this.troubleChoosen = in.readParcelable(CreateResCenterFormData.TroubleData.class.getClassLoader());
        this.productTroubleChoosenList = in.createTypedArrayList(PassProductTrouble.CREATOR);
        this.inputDescription = in.readString();
        this.solutionChoosen = in.readParcelable(CreateResCenterFormData.SolutionData.class.getClassLoader());
        this.refund = in.readString();
        this.attachmentData = in.createTypedArrayList(ResCenterAttachment.CREATOR);
        this.serverID = in.readString();
        this.uploadHost = in.readString();
        this.attachmentString = in.readString();
        this.postKey = in.readString();
        this.byPassFlag = in.readByte() != 0;
        this.fileUploaded = in.readString();
        this.actionResponseData = in.readParcelable(ActionResponseData.class.getClassLoader());
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
