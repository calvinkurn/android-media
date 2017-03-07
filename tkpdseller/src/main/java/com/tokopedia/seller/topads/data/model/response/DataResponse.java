package com.tokopedia.seller.topads.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class DataResponse<T> {

    @SerializedName("data")
    @Expose
    private T data;

    @SerializedName("eof")
    @Expose
    private boolean eof;

    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class Error {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("detail")
        @Expose
        private String detail;

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

    }
}
