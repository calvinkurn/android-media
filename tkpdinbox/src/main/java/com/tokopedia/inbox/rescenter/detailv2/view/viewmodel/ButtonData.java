package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ButtonData implements Parcelable{
    private boolean showEdit;
    private boolean showAcceptSolution;
    private boolean showAcceptProduct;
    private String acceptTextLite;
    private boolean acceptReturSolution;
    private boolean showAppealSolution;
    private boolean showAskHelp;
    private String askHelpDialogText;
    private boolean showCancel;
    private String cancelDialogText;
    private boolean showInputAddress;
    private boolean showInputAwb;
    private boolean showAcceptAdminSolution;
    private String acceptDialogText;

    private String askHelpLabel;
    private String cancelLabel;
    private String acceptLabel;
    private String editLabel;
    private String inputAddressLabel;
    private String appealLabel;
    private String inputAwbLabel;
    private String finishComplaintLabel;
    private String finishComplaintDialogText;

    private List<ButtonViewItem> buttonViewItemList;
    private int resolutionStatus;
    private boolean isCancelOn4thOrder;


    public boolean isCancelOn4thOrder() {
        return isCancelOn4thOrder;
    }

    public void setCancelOn4thOrder(boolean cancelOn4thOrder) {
        isCancelOn4thOrder = cancelOn4thOrder;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public boolean isShowAcceptSolution() {
        return showAcceptSolution;
    }

    public void setShowAcceptSolution(boolean showAcceptSolution) {
        this.showAcceptSolution = showAcceptSolution;
    }

    public boolean isShowAcceptProduct() {
        return showAcceptProduct;
    }

    public void setShowAcceptProduct(boolean showAcceptProduct) {
        this.showAcceptProduct = showAcceptProduct;
    }

    public boolean isAcceptReturSolution() {
        return acceptReturSolution;
    }

    public void setAcceptReturSolution(boolean acceptReturSolution) {
        this.acceptReturSolution = acceptReturSolution;
    }

    public boolean isShowAppealSolution() {
        return showAppealSolution;
    }

    public void setShowAppealSolution(boolean showAppealSolution) {
        this.showAppealSolution = showAppealSolution;
    }

    public boolean isShowAskHelp() {
        return showAskHelp;
    }

    public void setShowAskHelp(boolean showAskHelp) {
        this.showAskHelp = showAskHelp;
    }


    public void setAskHelpDialogText(String askHelpDialogText) {
        this.askHelpDialogText = askHelpDialogText;
    }

    public String getAskHelpDialogText() {
        return askHelpDialogText;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
    }

    public boolean isShowCancel() {
        return showCancel;
    }

    public void setCancelDialogText(String cancelDialogText) {
        this.cancelDialogText = cancelDialogText;
    }

    public String getCancelDialogText() {
        return cancelDialogText;
    }

    public void setShowInputAddress(boolean showInputAddress) {
        this.showInputAddress = showInputAddress;
    }

    public boolean isShowInputAddress() {
        return showInputAddress;
    }

    public void setShowInputAwb(boolean showInputAwb) {
        this.showInputAwb = showInputAwb;
    }

    public boolean isShowInputAwb() {
        return showInputAwb;
    }

    public void setShowAcceptAdminSolution(boolean showAcceptAdminSolution) {
        this.showAcceptAdminSolution = showAcceptAdminSolution;
    }

    public boolean isShowAcceptAdminSolution() {
        return showAcceptAdminSolution;
    }

    public void setAcceptProductDialogText(String acceptDialogText) {
        this.acceptDialogText = acceptDialogText;
    }

    public String getAcceptDialogText() {
        return acceptDialogText;
    }

    public void setAcceptDialogText(String acceptDialogText) {
        this.acceptDialogText = acceptDialogText;
    }

    public String getAskHelpLabel() {
        return askHelpLabel;
    }

    public void setAskHelpLabel(String askHelpLabel) {
        this.askHelpLabel = askHelpLabel;
    }

    public String getCancelLabel() {
        return cancelLabel;
    }

    public void setCancelLabel(String cancelLabel) {
        this.cancelLabel = cancelLabel;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
    }

    public String getEditLabel() {
        return editLabel;
    }

    public void setEditLabel(String editLabel) {
        this.editLabel = editLabel;
    }

    public String getInputAddressLabel() {
        return inputAddressLabel;
    }

    public void setInputAddressLabel(String inputAddressLabel) {
        this.inputAddressLabel = inputAddressLabel;
    }

    public String getAppealLabel() {
        return appealLabel;
    }

    public void setAppealLabel(String appealLabel) {
        this.appealLabel = appealLabel;
    }

    public String getInputAwbLabel() {
        return inputAwbLabel;
    }

    public void setInputAwbLabel(String inputAwbLabel) {
        this.inputAwbLabel = inputAwbLabel;
    }

    public String getAcceptTextLite() {
        return acceptTextLite;
    }

    public void setAcceptTextLite(String acceptTextLite) {
        this.acceptTextLite = acceptTextLite;
    }

    public ButtonData() {
    }

    public String getFinishComplaintLabel() {
        return finishComplaintLabel;
    }

    public void setFinishComplaintLabel(String finishComplaintLabel) {
        this.finishComplaintLabel = finishComplaintLabel;
    }


    public void setFinishComplaintDialogText(String finishComplaintDialogText) {
        this.finishComplaintDialogText = finishComplaintDialogText;
    }

    public String getFinishComplaintDialogText() {
        return finishComplaintDialogText;
    }

    public List<ButtonViewItem> getButtonViewItemList() {
        return buttonViewItemList;
    }

    public void setButtonViewItemList(List<ButtonViewItem> buttonViewItemList) {
        this.buttonViewItemList = buttonViewItemList;
    }

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.showEdit ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showAcceptSolution ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showAcceptProduct ? (byte) 1 : (byte) 0);
        dest.writeString(this.acceptTextLite);
        dest.writeByte(this.acceptReturSolution ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showAppealSolution ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showAskHelp ? (byte) 1 : (byte) 0);
        dest.writeString(this.askHelpDialogText);
        dest.writeByte(this.showCancel ? (byte) 1 : (byte) 0);
        dest.writeString(this.cancelDialogText);
        dest.writeByte(this.showInputAddress ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showInputAwb ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showAcceptAdminSolution ? (byte) 1 : (byte) 0);
        dest.writeString(this.acceptDialogText);
        dest.writeString(this.askHelpLabel);
        dest.writeString(this.cancelLabel);
        dest.writeString(this.acceptLabel);
        dest.writeString(this.editLabel);
        dest.writeString(this.inputAddressLabel);
        dest.writeString(this.appealLabel);
        dest.writeString(this.inputAwbLabel);
        dest.writeString(this.finishComplaintLabel);
        dest.writeString(this.finishComplaintDialogText);
        dest.writeTypedList(this.buttonViewItemList);
        dest.writeInt(this.resolutionStatus);
        dest.writeByte(this.isCancelOn4thOrder ? (byte) 1 : (byte) 0);
    }

    protected ButtonData(Parcel in) {
        this.showEdit = in.readByte() != 0;
        this.showAcceptSolution = in.readByte() != 0;
        this.showAcceptProduct = in.readByte() != 0;
        this.acceptTextLite = in.readString();
        this.acceptReturSolution = in.readByte() != 0;
        this.showAppealSolution = in.readByte() != 0;
        this.showAskHelp = in.readByte() != 0;
        this.askHelpDialogText = in.readString();
        this.showCancel = in.readByte() != 0;
        this.cancelDialogText = in.readString();
        this.showInputAddress = in.readByte() != 0;
        this.showInputAwb = in.readByte() != 0;
        this.showAcceptAdminSolution = in.readByte() != 0;
        this.acceptDialogText = in.readString();
        this.askHelpLabel = in.readString();
        this.cancelLabel = in.readString();
        this.acceptLabel = in.readString();
        this.editLabel = in.readString();
        this.inputAddressLabel = in.readString();
        this.appealLabel = in.readString();
        this.inputAwbLabel = in.readString();
        this.finishComplaintLabel = in.readString();
        this.finishComplaintDialogText = in.readString();
        this.buttonViewItemList = in.createTypedArrayList(ButtonViewItem.CREATOR);
        this.resolutionStatus = in.readInt();
        this.isCancelOn4thOrder = in.readByte() != 0;
    }

    public static final Creator<ButtonData> CREATOR = new Creator<ButtonData>() {
        @Override
        public ButtonData createFromParcel(Parcel source) {
            return new ButtonData(source);
        }

        @Override
        public ButtonData[] newArray(int size) {
            return new ButtonData[size];
        }
    };
}
