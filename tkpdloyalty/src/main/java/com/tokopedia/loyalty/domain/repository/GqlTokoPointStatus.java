package com.tokopedia.loyalty.domain.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/16/18.
 */

class GqlTokoPointStatus {

    @SerializedName("tier")
    @Expose
    private GqlTokoPointTier gqlTokoPointTier;

    @SerializedName("points")
    @Expose
    private GqlTokoPointPoints gqlTokoPointPoints;

    public GqlTokoPointTier getGqlTokoPointTier() {
        return gqlTokoPointTier;
    }

    public void setGqlTokoPointTier(GqlTokoPointTier gqlTokoPointTier) {
        this.gqlTokoPointTier = gqlTokoPointTier;
    }

    public GqlTokoPointPoints getGqlTokoPointPoints() {
        return gqlTokoPointPoints;
    }

    public void setGqlTokoPointPoints(GqlTokoPointPoints gqlTokoPointPoints) {
        this.gqlTokoPointPoints = gqlTokoPointPoints;
    }
}
