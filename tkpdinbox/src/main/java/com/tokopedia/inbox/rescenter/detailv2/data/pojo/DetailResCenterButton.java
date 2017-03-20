package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterButton {
    @SerializedName("report")
    private int report;
    @SerializedName("reportText")
    private String reportText;
    @SerializedName("cancel")
    private int cancel;
    @SerializedName("cancelText")
    private String cancelText;
    @SerializedName("edit")
    private int edit;
    @SerializedName("inputAddress")
    private int inputAddress;
    @SerializedName("appeal")
    private int appeal;
    @SerializedName("inputResi")
    private int inputResi;
    @SerializedName("accept")
    private int accept;
    @SerializedName("finish")
    private int finish;
    @SerializedName("acceptByAdmin")
    private int acceptByAdmin;
    @SerializedName("finishAcceptText")
    private String finishAcceptText;

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

    public int getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(int inputAddress) {
        this.inputAddress = inputAddress;
    }

    public int getAppeal() {
        return appeal;
    }

    public void setAppeal(int appeal) {
        this.appeal = appeal;
    }

    public int getInputResi() {
        return inputResi;
    }

    public void setInputResi(int inputResi) {
        this.inputResi = inputResi;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getAcceptByAdmin() {
        return acceptByAdmin;
    }

    public void setAcceptByAdmin(int acceptByAdmin) {
        this.acceptByAdmin = acceptByAdmin;
    }

    public String getFinishAcceptText() {
        return finishAcceptText;
    }

    public void setFinishAcceptText(String finishAcceptText) {
        this.finishAcceptText = finishAcceptText;
    }
}
