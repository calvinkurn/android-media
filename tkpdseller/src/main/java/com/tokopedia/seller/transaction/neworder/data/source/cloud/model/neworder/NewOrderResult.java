
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOrderResult {

    @SerializedName("data")
    @Expose
    private DataNewOrder dataNewOrder;
    @SerializedName("server_process_time")
    @Expose
    private double serverProcessTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error_message")
    @Expose
    private List<Object> errorMessage = null;

    public DataNewOrder getDataNewOrder() {
        return dataNewOrder;
    }

    public void setDataNewOrder(DataNewOrder dataNewOrder) {
        this.dataNewOrder = dataNewOrder;
    }

    public double getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(double serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<Object> errorMessage) {
        this.errorMessage = errorMessage;
    }

}
