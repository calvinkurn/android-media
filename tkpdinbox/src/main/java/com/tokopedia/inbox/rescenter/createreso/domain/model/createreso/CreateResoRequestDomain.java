package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class CreateResoRequestDomain {

    @Nullable
    private String orderId;

    @Nullable
    private String resolutionId;

    @Nullable
    private CreateValidateDomain createValidateDomain;

    @Nullable
    private GenerateHostDomain generateHostDomain;

    @Nullable
    private List<UploadDomain> uploadDomain;

    @Nullable
    private CreateSubmitDomain createSubmitDomain;

    @Nullable
    public CreateValidateDomain getCreateValidateDomain() {
        return createValidateDomain;
    }

    public void setCreateValidateDomain(@Nullable CreateValidateDomain createValidateDomain) {
        this.createValidateDomain = createValidateDomain;
    }

    @Nullable
    public GenerateHostDomain getGenerateHostDomain() {
        return generateHostDomain;
    }

    public void setGenerateHostDomain(@Nullable GenerateHostDomain generateHostDomain) {
        this.generateHostDomain = generateHostDomain;
    }

    @Nullable
    public List<UploadDomain> getUploadDomain() {
        return uploadDomain;
    }

    public void setUploadDomain(@Nullable List<UploadDomain> uploadDomain) {
        this.uploadDomain = uploadDomain;
    }

    @Nullable
    public CreateSubmitDomain getCreateSubmitDomain() {
        return createSubmitDomain;
    }

    public void setCreateSubmitDomain(@Nullable CreateSubmitDomain createSubmitDomain) {
        this.createSubmitDomain = createSubmitDomain;
    }

    @Nullable
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@Nullable String orderId) {
        this.orderId = orderId;
    }

    @Nullable
    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(@Nullable String resolutionId) {
        this.resolutionId = resolutionId;
    }
}
