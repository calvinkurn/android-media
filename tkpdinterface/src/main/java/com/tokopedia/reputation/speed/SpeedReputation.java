package com.tokopedia.reputation.speed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeedReputation {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private double serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server")
    @Expose
    private String server;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(double serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

}