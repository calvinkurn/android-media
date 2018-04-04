package com.tokopedia.inbox.inboxchat.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceSingleViewModel {
    String typeString;
    int type;
    String code;
    String createdTime;
    String description;
    String url;
    int id;
    String imageUrl;
    String status;
    int statusId;
    String title;
    String amount;

    public AttachInvoiceSingleViewModel(String typeString, int type, String code, String createdTime, String description, String url, int id, String imageUrl, String status, int statusId, String title, String amount) {
        this.typeString = typeString;
        this.type = type;
        this.code = code;
        this.createdTime = createdTime;
        this.description = description;
        this.url = url;
        this.id = id;
        this.imageUrl = imageUrl;
        this.status = status;
        this.statusId = statusId;
        this.title = title;
        this.amount = amount;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


}
