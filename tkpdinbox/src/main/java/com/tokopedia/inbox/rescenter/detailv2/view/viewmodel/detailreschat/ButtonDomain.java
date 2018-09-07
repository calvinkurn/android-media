package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ButtonDomain implements Parcelable {

    private int report;
    private String reportLabel;
    private String reportText;
    private int reportOrder;
    private int cancel;
    private String cancelLabel;
    private String cancelText;
    private int cancelOrder;
    private int edit;
    private String editLabel;
    private String editText;
    private int editOrder;
    private int inputAddress;
    private String inputAddressLabel;
    private String inputAddressText;
    private int inputAddressOrder;
    private int appeal;
    private String appealLabel;
    private String appealText;
    private int appealOrder;
    private int inputAWB;
    private String inputAWBLabel;
    private String inputAWBText;
    private int inputAWBOrder;
    private int accept;
    private String acceptLabel;
    private String acceptText;
    private String acceptTextLite;
    private int acceptOrder;
    private int acceptReturn;
    private String acceptReturnLabel;
    private String acceptReturnText;
    private int acceptReturnOrder;
    private int finish;
    private String finishLabel;
    private String finishText;
    private int finishOrder;
    private int acceptByAdmin;
    private String acceptByAdminLabel;
    private String acceptByAdminText;
    private int acceptByAdminOrder;
    private int acceptByAdminReturn;
    private String acceptByAdminReturnLabel;
    private String acceptByAdminReturnText;
    private int acceptByAdminReturnOrder;
    private int recomplaint;
    private String recomplainLabel;
    private String recomplaintText;
    private int recomplaintOrder;

    public ButtonDomain(int report,
                        String reportLabel,
                        String reportText,
                        int reportOrder,
                        int cancel,
                        String cancelLabel,
                        String cancelText,
                        int cancelOrder,
                        int edit,
                        String editLabel,
                        String editText,
                        int editOrder,
                        int inputAddress,
                        String inputAddressLabel,
                        String inputAddressText,
                        int inputAddressOrder,
                        int appeal,
                        String appealLabel,
                        String appealText,
                        int appealOrder,
                        int inputAWB,
                        String inputAWBLabel,
                        String inputAWBText,
                        int inputAWBOrder,
                        int accept,
                        String acceptLabel,
                        String acceptText,
                        String acceptTextLite,
                        int acceptOrder,
                        int acceptReturn,
                        String acceptReturnLabel,
                        String acceptReturnText,
                        int acceptReturnOrder,
                        int finish,
                        String finishLabel,
                        String finishText,
                        int finishOrder,
                        int acceptByAdmin,
                        String acceptByAdminLabel,
                        String acceptByAdminText,
                        int acceptByAdminOrder,
                        int acceptByAdminReturn,
                        String acceptByAdminReturnLabel,
                        String acceptByAdminReturnText,
                        int acceptByAdminReturnOrder,
                        int recomplaint,
                        String recomplainLabel,
                        String recomplaintText,
                        int recomplaintOrder) {
        this.report = report;
        this.reportLabel = reportLabel;
        this.reportText = reportText;
        this.reportOrder = reportOrder;
        this.cancel = cancel;
        this.cancelLabel = cancelLabel;
        this.cancelText = cancelText;
        this.cancelOrder = cancelOrder;
        this.edit = edit;
        this.editLabel = editLabel;
        this.editText = editText;
        this.editOrder = editOrder;
        this.inputAddress = inputAddress;
        this.inputAddressLabel = inputAddressLabel;
        this.inputAddressText = inputAddressText;
        this.inputAddressOrder = inputAddressOrder;
        this.appeal = appeal;
        this.appealLabel = appealLabel;
        this.appealText = appealText;
        this.appealOrder = appealOrder;
        this.inputAWB = inputAWB;
        this.inputAWBLabel = inputAWBLabel;
        this.inputAWBText = inputAWBText;
        this.inputAWBOrder = inputAWBOrder;
        this.accept = accept;
        this.acceptLabel = acceptLabel;
        this.acceptText = acceptText;
        this.acceptTextLite = acceptTextLite;
        this.acceptOrder = acceptOrder;
        this.acceptReturn = acceptReturn;
        this.acceptReturnLabel = acceptReturnLabel;
        this.acceptReturnText = acceptReturnText;
        this.acceptReturnOrder = acceptReturnOrder;
        this.finish = finish;
        this.finishLabel = finishLabel;
        this.finishText = finishText;
        this.finishOrder = finishOrder;
        this.acceptByAdmin = acceptByAdmin;
        this.acceptByAdminLabel = acceptByAdminLabel;
        this.acceptByAdminText = acceptByAdminText;
        this.acceptByAdminOrder = acceptByAdminOrder;
        this.acceptByAdminReturn = acceptByAdminReturn;
        this.acceptByAdminReturnLabel = acceptByAdminReturnLabel;
        this.acceptByAdminReturnText = acceptByAdminReturnText;
        this.acceptByAdminReturnOrder = acceptByAdminReturnOrder;
        this.recomplaint = recomplaint;
        this.recomplainLabel = recomplainLabel;
        this.recomplaintText = recomplaintText;
        this.recomplaintOrder = recomplaintOrder;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getReportLabel() {
        return reportLabel;
    }

    public void setReportLabel(String reportLabel) {
        this.reportLabel = reportLabel;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public int getReportOrder() {
        return reportOrder;
    }

    public void setReportOrder(int reportOrder) {
        this.reportOrder = reportOrder;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public String getCancelLabel() {
        return cancelLabel;
    }

    public void setCancelLabel(String cancelLabel) {
        this.cancelLabel = cancelLabel;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public int getCancelOrder() {
        return cancelOrder;
    }

    public void setCancelOrder(int cancelOrder) {
        this.cancelOrder = cancelOrder;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public String getEditLabel() {
        return editLabel;
    }

    public void setEditLabel(String editLabel) {
        this.editLabel = editLabel;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }

    public int getEditOrder() {
        return editOrder;
    }

    public void setEditOrder(int editOrder) {
        this.editOrder = editOrder;
    }

    public int getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(int inputAddress) {
        this.inputAddress = inputAddress;
    }

    public String getInputAddressLabel() {
        return inputAddressLabel;
    }

    public void setInputAddressLabel(String inputAddressLabel) {
        this.inputAddressLabel = inputAddressLabel;
    }

    public String getInputAddressText() {
        return inputAddressText;
    }

    public void setInputAddressText(String inputAddressText) {
        this.inputAddressText = inputAddressText;
    }

    public int getInputAddressOrder() {
        return inputAddressOrder;
    }

    public void setInputAddressOrder(int inputAddressOrder) {
        this.inputAddressOrder = inputAddressOrder;
    }

    public String getAcceptTextLite() {
        return acceptTextLite;
    }

    public void setAcceptTextLite(String acceptTextLite) {
        this.acceptTextLite = acceptTextLite;
    }

    public int getAppeal() {
        return appeal;
    }

    public void setAppeal(int appeal) {
        this.appeal = appeal;
    }

    public String getAppealLabel() {
        return appealLabel;
    }

    public void setAppealLabel(String appealLabel) {
        this.appealLabel = appealLabel;
    }

    public String getAppealText() {
        return appealText;
    }

    public void setAppealText(String appealText) {
        this.appealText = appealText;
    }

    public int getAppealOrder() {
        return appealOrder;
    }

    public void setAppealOrder(int appealOrder) {
        this.appealOrder = appealOrder;
    }

    public int getInputAWB() {
        return inputAWB;
    }

    public void setInputAWB(int inputAWB) {
        this.inputAWB = inputAWB;
    }

    public String getInputAWBLabel() {
        return inputAWBLabel;
    }

    public void setInputAWBLabel(String inputAWBLabel) {
        this.inputAWBLabel = inputAWBLabel;
    }

    public String getInputAWBText() {
        return inputAWBText;
    }

    public void setInputAWBText(String inputAWBText) {
        this.inputAWBText = inputAWBText;
    }

    public int getInputAWBOrder() {
        return inputAWBOrder;
    }

    public void setInputAWBOrder(int inputAWBOrder) {
        this.inputAWBOrder = inputAWBOrder;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
    }

    public String getAcceptText() {
        return acceptText;
    }

    public void setAcceptText(String acceptText) {
        this.acceptText = acceptText;
    }

    public int getAcceptOrder() {
        return acceptOrder;
    }

    public void setAcceptOrder(int acceptOrder) {
        this.acceptOrder = acceptOrder;
    }

    public int getAcceptReturn() {
        return acceptReturn;
    }

    public void setAcceptReturn(int acceptReturn) {
        this.acceptReturn = acceptReturn;
    }

    public String getAcceptReturnLabel() {
        return acceptReturnLabel;
    }

    public void setAcceptReturnLabel(String acceptReturnLabel) {
        this.acceptReturnLabel = acceptReturnLabel;
    }

    public String getAcceptReturnText() {
        return acceptReturnText;
    }

    public void setAcceptReturnText(String acceptReturnText) {
        this.acceptReturnText = acceptReturnText;
    }

    public int getAcceptReturnOrder() {
        return acceptReturnOrder;
    }

    public void setAcceptReturnOrder(int acceptReturnOrder) {
        this.acceptReturnOrder = acceptReturnOrder;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getFinishLabel() {
        return finishLabel;
    }

    public void setFinishLabel(String finishLabel) {
        this.finishLabel = finishLabel;
    }

    public String getFinishText() {
        return finishText;
    }

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    public int getFinishOrder() {
        return finishOrder;
    }

    public void setFinishOrder(int finishOrder) {
        this.finishOrder = finishOrder;
    }

    public int getAcceptByAdmin() {
        return acceptByAdmin;
    }

    public void setAcceptByAdmin(int acceptByAdmin) {
        this.acceptByAdmin = acceptByAdmin;
    }

    public String getAcceptByAdminLabel() {
        return acceptByAdminLabel;
    }

    public void setAcceptByAdminLabel(String acceptByAdminLabel) {
        this.acceptByAdminLabel = acceptByAdminLabel;
    }

    public String getAcceptByAdminText() {
        return acceptByAdminText;
    }

    public void setAcceptByAdminText(String acceptByAdminText) {
        this.acceptByAdminText = acceptByAdminText;
    }

    public int getAcceptByAdminOrder() {
        return acceptByAdminOrder;
    }

    public void setAcceptByAdminOrder(int acceptByAdminOrder) {
        this.acceptByAdminOrder = acceptByAdminOrder;
    }

    public int getAcceptByAdminReturn() {
        return acceptByAdminReturn;
    }

    public void setAcceptByAdminReturn(int acceptByAdminReturn) {
        this.acceptByAdminReturn = acceptByAdminReturn;
    }

    public String getAcceptByAdminReturnLabel() {
        return acceptByAdminReturnLabel;
    }

    public void setAcceptByAdminReturnLabel(String acceptByAdminReturnLabel) {
        this.acceptByAdminReturnLabel = acceptByAdminReturnLabel;
    }

    public String getAcceptByAdminReturnText() {
        return acceptByAdminReturnText;
    }

    public void setAcceptByAdminReturnText(String acceptByAdminReturnText) {
        this.acceptByAdminReturnText = acceptByAdminReturnText;
    }

    public int getAcceptByAdminReturnOrder() {
        return acceptByAdminReturnOrder;
    }

    public void setAcceptByAdminReturnOrder(int acceptByAdminReturnOrder) {
        this.acceptByAdminReturnOrder = acceptByAdminReturnOrder;
    }

    public int getRecomplaint() {
        return recomplaint;
    }

    public void setRecomplaint(int recomplaint) {
        this.recomplaint = recomplaint;
    }

    public String getRecomplainLabel() {
        return recomplainLabel;
    }

    public void setRecomplainLabel(String recomplainLabel) {
        this.recomplainLabel = recomplainLabel;
    }

    public String getRecomplaintText() {
        return recomplaintText;
    }

    public void setRecomplaintText(String recomplaintText) {
        this.recomplaintText = recomplaintText;
    }

    public int getRecomplaintOrder() {
        return recomplaintOrder;
    }

    public void setRecomplaintOrder(int recomplaintOrder) {
        this.recomplaintOrder = recomplaintOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.report);
        dest.writeString(this.reportLabel);
        dest.writeString(this.reportText);
        dest.writeInt(this.reportOrder);
        dest.writeInt(this.cancel);
        dest.writeString(this.cancelLabel);
        dest.writeString(this.cancelText);
        dest.writeInt(this.cancelOrder);
        dest.writeInt(this.edit);
        dest.writeString(this.editLabel);
        dest.writeString(this.editText);
        dest.writeInt(this.editOrder);
        dest.writeInt(this.inputAddress);
        dest.writeString(this.inputAddressLabel);
        dest.writeString(this.inputAddressText);
        dest.writeInt(this.inputAddressOrder);
        dest.writeInt(this.appeal);
        dest.writeString(this.appealLabel);
        dest.writeString(this.appealText);
        dest.writeInt(this.appealOrder);
        dest.writeInt(this.inputAWB);
        dest.writeString(this.inputAWBLabel);
        dest.writeString(this.inputAWBText);
        dest.writeInt(this.inputAWBOrder);
        dest.writeInt(this.accept);
        dest.writeString(this.acceptLabel);
        dest.writeString(this.acceptText);
        dest.writeInt(this.acceptOrder);
        dest.writeInt(this.acceptReturn);
        dest.writeString(this.acceptReturnLabel);
        dest.writeString(this.acceptReturnText);
        dest.writeInt(this.acceptReturnOrder);
        dest.writeInt(this.finish);
        dest.writeString(this.finishLabel);
        dest.writeString(this.finishText);
        dest.writeInt(this.finishOrder);
        dest.writeInt(this.acceptByAdmin);
        dest.writeString(this.acceptByAdminLabel);
        dest.writeString(this.acceptByAdminText);
        dest.writeInt(this.acceptByAdminOrder);
        dest.writeInt(this.acceptByAdminReturn);
        dest.writeString(this.acceptByAdminReturnLabel);
        dest.writeString(this.acceptByAdminReturnText);
        dest.writeInt(this.acceptByAdminReturnOrder);
        dest.writeInt(this.recomplaint);
        dest.writeString(this.recomplainLabel);
        dest.writeString(this.recomplaintText);
        dest.writeInt(this.recomplaintOrder);
    }

    protected ButtonDomain(Parcel in) {
        this.report = in.readInt();
        this.reportLabel = in.readString();
        this.reportText = in.readString();
        this.reportOrder = in.readInt();
        this.cancel = in.readInt();
        this.cancelLabel = in.readString();
        this.cancelText = in.readString();
        this.cancelOrder = in.readInt();
        this.edit = in.readInt();
        this.editLabel = in.readString();
        this.editText = in.readString();
        this.editOrder = in.readInt();
        this.inputAddress = in.readInt();
        this.inputAddressLabel = in.readString();
        this.inputAddressText = in.readString();
        this.inputAddressOrder = in.readInt();
        this.appeal = in.readInt();
        this.appealLabel = in.readString();
        this.appealText = in.readString();
        this.appealOrder = in.readInt();
        this.inputAWB = in.readInt();
        this.inputAWBLabel = in.readString();
        this.inputAWBText = in.readString();
        this.inputAWBOrder = in.readInt();
        this.accept = in.readInt();
        this.acceptLabel = in.readString();
        this.acceptText = in.readString();
        this.acceptOrder = in.readInt();
        this.acceptReturn = in.readInt();
        this.acceptReturnLabel = in.readString();
        this.acceptReturnText = in.readString();
        this.acceptReturnOrder = in.readInt();
        this.finish = in.readInt();
        this.finishLabel = in.readString();
        this.finishText = in.readString();
        this.finishOrder = in.readInt();
        this.acceptByAdmin = in.readInt();
        this.acceptByAdminLabel = in.readString();
        this.acceptByAdminText = in.readString();
        this.acceptByAdminOrder = in.readInt();
        this.acceptByAdminReturn = in.readInt();
        this.acceptByAdminReturnLabel = in.readString();
        this.acceptByAdminReturnText = in.readString();
        this.acceptByAdminReturnOrder = in.readInt();
        this.recomplaint = in.readInt();
        this.recomplainLabel = in.readString();
        this.recomplaintText = in.readString();
        this.recomplaintOrder = in.readInt();
    }

    public static final Creator<ButtonDomain> CREATOR = new Creator<ButtonDomain>() {
        @Override
        public ButtonDomain createFromParcel(Parcel source) {
            return new ButtonDomain(source);
        }

        @Override
        public ButtonDomain[] newArray(int size) {
            return new ButtonDomain[size];
        }
    };
}
