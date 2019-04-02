package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemResponseDomain {
    @Nullable
    private List<ProductProblemDomain> productProblemDomainList = new ArrayList<>();

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String errMessage;

    public ProductProblemResponseDomain(@Nullable List<ProductProblemDomain> productProblemDomainList) {
        this.productProblemDomainList = productProblemDomainList;
    }

    @Nullable
    public List<ProductProblemDomain> getProductProblemDomainList() {
        return productProblemDomainList;
    }

    public void setProductProblemDomainList(@Nullable List<ProductProblemDomain> productProblemDomainList) {
        this.productProblemDomainList = productProblemDomainList;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }

    @Nullable
    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(@Nullable String errMessage) {
        this.errMessage = errMessage;
    }
}
