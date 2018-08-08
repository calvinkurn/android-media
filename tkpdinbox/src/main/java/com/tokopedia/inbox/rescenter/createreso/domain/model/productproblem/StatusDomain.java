package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusDomain {
    @Nullable
    private boolean delivered;
    @Nullable
    private String name;
    @Nullable
    private List<StatusTroubleDomain> statusTroubleDomainList;
    @Nullable
    private StatusInfoDomain statusInfoDomain;

    public StatusDomain(@Nullable boolean delivered,
                        @Nullable String name,
                        @Nullable List<StatusTroubleDomain> statusTroubleDomainList,
                        @Nullable StatusInfoDomain statusInfoDomain) {
        this.delivered = delivered;
        this.name = name;
        this.statusTroubleDomainList = statusTroubleDomainList;
        this.statusInfoDomain = statusInfoDomain;
    }

    @Nullable
    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(@Nullable boolean delivered) {
        this.delivered = delivered;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public List<StatusTroubleDomain> getStatusTroubleDomainList() {
        return statusTroubleDomainList;
    }

    public void setStatusTroubleDomainList(@Nullable List<StatusTroubleDomain> statusTroubleDomainList) {
        this.statusTroubleDomainList = statusTroubleDomainList;
    }

    @Nullable
    public StatusInfoDomain getStatusInfoDomain() {
        return statusInfoDomain;
    }

    public void setStatusInfoDomain(@Nullable StatusInfoDomain statusInfoDomain) {
        this.statusInfoDomain = statusInfoDomain;
    }
}
