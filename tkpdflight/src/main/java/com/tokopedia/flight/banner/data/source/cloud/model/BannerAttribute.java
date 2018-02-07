package com.tokopedia.flight.banner.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 27/12/17.
 */

public class BannerAttribute {
    @SerializedName("recharge_cmsbanner_id")
    @Expose
    private int rechargeCmsbannerId;

    @SerializedName("file_name")
    @Expose
    private String fileName;

    @SerializedName("file_name_webp")
    @Expose
    private String fileNameWebp;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    @SerializedName("data_title")
    @Expose
    private String dataTitle;

    private int priority;
    private int status;
    private String title;
    private String subtitle;
    private String promocode;

    public int getRechargeCmsbannerId() {
        return rechargeCmsbannerId;
    }

    public void setRechargeCmsbannerId(int rechargeCmsbannerId) {
        this.rechargeCmsbannerId = rechargeCmsbannerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameWebp() {
        return fileNameWebp;
    }

    public void setFileNameWebp(String fileNameWebp) {
        this.fileNameWebp = fileNameWebp;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}
