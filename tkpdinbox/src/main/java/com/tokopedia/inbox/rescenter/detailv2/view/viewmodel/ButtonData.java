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
}
