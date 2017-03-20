package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/17/17.
 */

public class SolutionDomainModel {
    private String solutionRemark;
    private String solutionDate;
    private String solutionActionBy;

    public String getSolutionRemark() {
        return solutionRemark;
    }

    public void setSolutionRemark(String solutionRemark) {
        this.solutionRemark = solutionRemark;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getSolutionActionBy() {
        return solutionActionBy;
    }

    public void setSolutionActionBy(String solutionActionBy) {
        this.solutionActionBy = solutionActionBy;
    }
}
