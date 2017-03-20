package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ButtonDomainModel {
    private int report;
    private String reportDialogText;
    private int cancel;
    private String cancelDialogText;
    private int edit;
    private int inputAddress;
    private int appeal;
    private int inputAwb;
    private int finish;
    private int accept;
    private int acceptByAdmin;
    private String acceptProductDialogText;

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getReportDialogText() {
        return reportDialogText;
    }

    public void setReportDialogText(String reportDialogText) {
        this.reportDialogText = reportDialogText;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public String getCancelDialogText() {
        return cancelDialogText;
    }

    public void setCancelDialogText(String cancelDialogText) {
        this.cancelDialogText = cancelDialogText;
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

    public int getInputAwb() {
        return inputAwb;
    }

    public void setInputAwb(int inputAwb) {
        this.inputAwb = inputAwb;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getAcceptByAdmin() {
        return acceptByAdmin;
    }

    public void setAcceptByAdmin(int acceptByAdmin) {
        this.acceptByAdmin = acceptByAdmin;
    }


    public String getAcceptProductDialogText() {
        return acceptProductDialogText;
    }

    public void setAcceptProductDialogText(String acceptProductDialogText) {
        this.acceptProductDialogText = acceptProductDialogText;
    }
}
