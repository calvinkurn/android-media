package com.tokopedia.transaction.addtocart.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LogisticsData {

    @SerializedName("data")
    @Expose
    private OngkirData ongkirData;

    @SerializedName("errors")
    @Expose
    private List<LogisticsError> logisticsError;

    public List<LogisticsError> getLogisticsError() {
        return logisticsError;
    }

    public void setLogisticsError(List<LogisticsError> logisticsError) {
        this.logisticsError = logisticsError;
    }

    public OngkirData getOngkirData() {
        return ongkirData;
    }

    public void setOngkirData(OngkirData ongkirData) {
        this.ongkirData = ongkirData;
    }
}
