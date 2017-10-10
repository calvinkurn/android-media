package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ButtonResponse {

    @SerializedName("report")
    private int report;

    @SerializedName("reportText")
    private String reportText;

    @SerializedName("cancelComplaint")
    private int cancel;

    @SerializedName("cancelComplaintText")
    private String cancelText;

    @SerializedName("editSolution")
    private int edit;

    @SerializedName("editSolutionText")
    private String editText;

    @SerializedName("inputAddress")
    private int inputAddress;

    @SerializedName("inputAddressText")
    private String inputAddressText;

    @SerializedName("appealSolution")
    private int appeal;

    @SerializedName("appealSolutionText")
    private String appealText;

    @SerializedName("inputAWB")
    private int inputAWB;

    @SerializedName("inputAWBText")
    private String inputAWBText;

    @SerializedName("acceptSolution")
    private int accept;

    @SerializedName("acceptSolutionText")
    private String acceptText;

    @SerializedName("AcceptSolutionRetur")
    private int acceptReturn;

    @SerializedName("AcceptSolutionReturText")
    private String acceptReturnText;

    @SerializedName("finishComplaint")
    private int finish;

    @SerializedName("finishComplaintText")
    private String finishText;

    @SerializedName("acceptSolutionAdmin")
    private int acceptByAdmin;

    @SerializedName("acceptSolutionAdminText")
    private String acceptByAdminText;

    @SerializedName("acceptSolutionAdminRetur")
    private int acceptByAdminReturn;

    @SerializedName("acceptSolutionAdminReturText")
    private String acceptByAdminReturnText;

    @SerializedName("")
    private int recomplaint;

    @SerializedName("")
    private String recomplaintText;

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }

    public int getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(int inputAddress) {
        this.inputAddress = inputAddress;
    }

    public String getInputAddressText() {
        return inputAddressText;
    }

    public void setInputAddressText(String inputAddressText) {
        this.inputAddressText = inputAddressText;
    }

    public int getAppeal() {
        return appeal;
    }

    public void setAppeal(int appeal) {
        this.appeal = appeal;
    }

    public String getAppealText() {
        return appealText;
    }

    public void setAppealText(String appealText) {
        this.appealText = appealText;
    }

    public int getInputAWB() {
        return inputAWB;
    }

    public void setInputAWB(int inputAWB) {
        this.inputAWB = inputAWB;
    }

    public String getInputAWBText() {
        return inputAWBText;
    }

    public void setInputAWBText(String inputAWBText) {
        this.inputAWBText = inputAWBText;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public String getAcceptText() {
        return acceptText;
    }

    public void setAcceptText(String acceptText) {
        this.acceptText = acceptText;
    }

    public int getAcceptReturn() {
        return acceptReturn;
    }

    public void setAcceptReturn(int acceptReturn) {
        this.acceptReturn = acceptReturn;
    }

    public String getAcceptReturnText() {
        return acceptReturnText;
    }

    public void setAcceptReturnText(String acceptReturnText) {
        this.acceptReturnText = acceptReturnText;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getFinishText() {
        return finishText;
    }

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    public int getAcceptByAdmin() {
        return acceptByAdmin;
    }

    public void setAcceptByAdmin(int acceptByAdmin) {
        this.acceptByAdmin = acceptByAdmin;
    }

    public String getAcceptByAdminText() {
        return acceptByAdminText;
    }

    public void setAcceptByAdminText(String acceptByAdminText) {
        this.acceptByAdminText = acceptByAdminText;
    }

    public int getAcceptByAdminReturn() {
        return acceptByAdminReturn;
    }

    public void setAcceptByAdminReturn(int acceptByAdminReturn) {
        this.acceptByAdminReturn = acceptByAdminReturn;
    }

    public String getAcceptByAdminReturnText() {
        return acceptByAdminReturnText;
    }

    public void setAcceptByAdminReturnText(String acceptByAdminReturnText) {
        this.acceptByAdminReturnText = acceptByAdminReturnText;
    }

    public int getRecomplaint() {
        return recomplaint;
    }

    public void setRecomplaint(int recomplaint) {
        this.recomplaint = recomplaint;
    }

    public String getRecomplaintText() {
        return recomplaintText;
    }

    public void setRecomplaintText(String recomplaintText) {
        this.recomplaintText = recomplaintText;
    }
}
