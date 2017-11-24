
package com.tokopedia.seller.shop.district.data.source.cloud.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProvincesCitiesDistrict {

    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("province_name")
    @Expose
    private String provinceName;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
