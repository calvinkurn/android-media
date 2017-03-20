package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ButtonData {
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
}
