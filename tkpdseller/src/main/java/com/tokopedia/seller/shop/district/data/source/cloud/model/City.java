
package com.tokopedia.seller.shop.district.data.source.cloud.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("districts")
    @Expose
    private List<District> districts = null;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

}
