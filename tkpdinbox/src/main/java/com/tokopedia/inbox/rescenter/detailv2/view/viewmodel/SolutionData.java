package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/10/17.
 */

public class SolutionData {
    private String solutionText;
    private String solutionDate;
    private String solutionProvider;
    private boolean editAble;

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getSolutionProvider() {
        return solutionProvider;
    }

    public void setSolutionProvider(String solutionProvider) {
        this.solutionProvider = solutionProvider;
    }

    public boolean isEditAble() {
        return editAble;
    }

    public void setEditAble(boolean editAble) {
        this.editAble = editAble;
    }
}
