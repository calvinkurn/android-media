package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ButtonData {
    private boolean showEdit;
    private boolean showAcceptSolution;
    private boolean showAcceptProduct;

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
}
