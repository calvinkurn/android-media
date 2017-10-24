
package com.tokopedia.seller.shop.district.data.source.cloud.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("provinces_cities_districts")
    @Expose
    private List<ProvincesCitiesDistrict> provincesCitiesDistricts = null;
    @SerializedName("cannot_create")
    @Expose
    private CannotCreate cannotCreate;

    public List<ProvincesCitiesDistrict> getProvincesCitiesDistricts() {
        return provincesCitiesDistricts;
    }

    public void setProvincesCitiesDistricts(List<ProvincesCitiesDistrict> provincesCitiesDistricts) {
        this.provincesCitiesDistricts = provincesCitiesDistricts;
    }

    public CannotCreate getCannotCreate() {
        return cannotCreate;
    }

    public void setCannotCreate(CannotCreate cannotCreate) {
        this.cannotCreate = cannotCreate;
    }

}
