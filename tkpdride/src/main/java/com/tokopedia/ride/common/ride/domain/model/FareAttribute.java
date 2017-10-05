package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 5/3/17.
 */

public class FareAttribute {
    private String code;
    private String title;
    private String detail;
    private String detailId;
    private String detailEn;
    private String message;
    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public void setDetailEn(String detailEn) {
        this.detailEn = detailEn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
