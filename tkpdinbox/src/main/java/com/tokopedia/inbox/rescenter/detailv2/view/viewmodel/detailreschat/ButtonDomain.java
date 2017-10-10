package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ButtonDomain {

    private int report;
    private String reportText;
    private int cancel;
    private String cancelText;
    private int edit;
    private int inputAddress;
    private int appeal;
    private int inputResi;
    private int accept;
    private int acceptReturn;
    private int finish;
    private int acceptByAdmin;
    private int acceptByAdminReturn;
    private String finishAcceptText;
    private int recomplain;

    public ButtonDomain(int report,
                        String reportText,
                        int cancel,
                        String cancelText,
                        int edit,
                        int inputAddress,
                        int appeal,
                        int inputResi,
                        int accept,
                        int acceptReturn,
                        int finish,
                        int acceptByAdmin,
                        int acceptByAdminReturn,
                        String finishAcceptText,
                        int recomplain) {
        this.report = report;
        this.reportText = reportText;
        this.cancel = cancel;
        this.cancelText = cancelText;
        this.edit = edit;
        this.inputAddress = inputAddress;
        this.appeal = appeal;
        this.inputResi = inputResi;
        this.accept = accept;
        this.acceptReturn = acceptReturn;
        this.finish = finish;
        this.acceptByAdmin = acceptByAdmin;
        this.acceptByAdminReturn = acceptByAdminReturn;
        this.finishAcceptText = finishAcceptText;
        this.recomplain = recomplain;
    }

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

    public int getAcceptReturn() {
        return acceptReturn;
    }

    public void setAcceptReturn(int acceptReturn) {
        this.acceptReturn = acceptReturn;
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

    public int getAcceptByAdminReturn() {
        return acceptByAdminReturn;
    }

    public void setAcceptByAdminReturn(int acceptByAdminReturn) {
        this.acceptByAdminReturn = acceptByAdminReturn;
    }

    public String getFinishAcceptText() {
        return finishAcceptText;
    }

    public void setFinishAcceptText(String finishAcceptText) {
        this.finishAcceptText = finishAcceptText;
    }

    public int getRecomplain() {
        return recomplain;
    }

    public void setRecomplain(int recomplain) {
        this.recomplain = recomplain;
    }
}
