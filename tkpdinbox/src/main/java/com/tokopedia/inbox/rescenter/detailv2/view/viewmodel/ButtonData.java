package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ButtonData implements Parcelable{
    private boolean showEdit;
    private boolean showAcceptSolution;
    private boolean showAcceptProduct;
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


    protected ButtonData(Parcel in) {
        showEdit = in.readByte() != 0;
        showAcceptSolution = in.readByte() != 0;
        showAcceptProduct = in.readByte() != 0;
        acceptReturSolution = in.readByte() != 0;
        showAppealSolution = in.readByte() != 0;
        showAskHelp = in.readByte() != 0;
        askHelpDialogText = in.readString();
        showCancel = in.readByte() != 0;
        cancelDialogText = in.readString();
        showInputAddress = in.readByte() != 0;
        showInputAwb = in.readByte() != 0;
        showAcceptAdminSolution = in.readByte() != 0;
        acceptDialogText = in.readString();
        askHelpLabel = in.readString();
        cancelLabel = in.readString();
        acceptLabel = in.readString();
        editLabel = in.readString();
        inputAddressLabel = in.readString();
        appealLabel = in.readString();
        inputAwbLabel = in.readString();
        finishComplaintLabel = in.readString();
        finishComplaintDialogText = in.readString();
    }

    public static final Creator<ButtonData> CREATOR = new Creator<ButtonData>() {
        @Override
        public ButtonData createFromParcel(Parcel in) {
            return new ButtonData(in);
        }

        @Override
        public ButtonData[] newArray(int size) {
            return new ButtonData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (showEdit ? 1 : 0));
        dest.writeByte((byte) (showAcceptSolution ? 1 : 0));
        dest.writeByte((byte) (showAcceptProduct ? 1 : 0));
        dest.writeByte((byte) (acceptReturSolution ? 1 : 0));
        dest.writeByte((byte) (showAppealSolution ? 1 : 0));
        dest.writeByte((byte) (showAskHelp ? 1 : 0));
        dest.writeString(askHelpDialogText);
        dest.writeByte((byte) (showCancel ? 1 : 0));
        dest.writeString(cancelDialogText);
        dest.writeByte((byte) (showInputAddress ? 1 : 0));
        dest.writeByte((byte) (showInputAwb ? 1 : 0));
        dest.writeByte((byte) (showAcceptAdminSolution ? 1 : 0));
        dest.writeString(acceptDialogText);
        dest.writeString(askHelpLabel);
        dest.writeString(cancelLabel);
        dest.writeString(acceptLabel);
        dest.writeString(editLabel);
        dest.writeString(inputAddressLabel);
        dest.writeString(appealLabel);
        dest.writeString(inputAwbLabel);
        dest.writeString(finishComplaintLabel);
        dest.writeString(finishComplaintDialogText);
    }
}
