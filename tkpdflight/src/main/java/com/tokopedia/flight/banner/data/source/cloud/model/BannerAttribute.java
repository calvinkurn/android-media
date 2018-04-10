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
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("promoCode")
    @Expose
    private String promoCode;

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

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @Override
    public String toString() {
        return "BannerAttribute{" +
                "rechargeCmsbannerId=" + rechargeCmsbannerId +
                ", fileName='" + fileName + '\'' +
                ", fileNameWebp='" + fileNameWebp + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", dataTitle='" + dataTitle + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", promoCode='" + promoCode + '\'' +
                '}';
    }
}
