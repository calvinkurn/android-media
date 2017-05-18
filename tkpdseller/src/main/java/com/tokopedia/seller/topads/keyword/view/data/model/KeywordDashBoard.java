package com.tokopedia.seller.topads.keyword.view.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashBoard {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("page")
    @Expose
    private Page_ page;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Page_ getPage() {
        return page;
    }

    public void setPage(Page_ page) {
        this.page = page;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public static class Meta {

        @SerializedName("page")
        @Expose
        private Page page;

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

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

    public static class Datum {

        @SerializedName("keyword_id")
        @Expose
        private int keywordId;
        @SerializedName("keyword_tag")
        @Expose
        private String keywordTag;
        @SerializedName("group_id")
        @Expose
        private int groupId;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("keyword_status")
        @Expose
        private int keywordStatus;
        @SerializedName("keyword_status_desc")
        @Expose
        private String keywordStatusDesc;
        @SerializedName("keyword_type_id")
        @Expose
        private String keywordTypeId;
        @SerializedName("keyword_type_desc")
        @Expose
        private String keywordTypeDesc;
        @SerializedName("keyword_status_toogle")
        @Expose
        private int keywordStatusToogle;
        @SerializedName("keyword_price_bid_fmt")
        @Expose
        private String keywordPriceBidFmt;
        @SerializedName("stat_avg_click")
        @Expose
        private String statAvgClick;
        @SerializedName("stat_total_spent")
        @Expose
        private String statTotalSpent;
        @SerializedName("stat_total_impression")
        @Expose
        private String statTotalImpression;
        @SerializedName("stat_total_click")
        @Expose
        private String statTotalClick;
        @SerializedName("stat_total_ctr")
        @Expose
        private String statTotalCtr;
        @SerializedName("stat_total_conversion")
        @Expose
        private String statTotalConversion;
        @SerializedName("label_edit")
        @Expose
        private String labelEdit;
        @SerializedName("label_per_click")
        @Expose
        private String labelPerClick;
        @SerializedName("label_of")
        @Expose
        private String labelOf;

        public int getKeywordId() {
            return keywordId;
        }

        public void setKeywordId(int keywordId) {
            this.keywordId = keywordId;
        }

        public String getKeywordTag() {
            return keywordTag;
        }

        public void setKeywordTag(String keywordTag) {
            this.keywordTag = keywordTag;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getKeywordStatus() {
            return keywordStatus;
        }

        public void setKeywordStatus(int keywordStatus) {
            this.keywordStatus = keywordStatus;
        }

        public String getKeywordStatusDesc() {
            return keywordStatusDesc;
        }

        public void setKeywordStatusDesc(String keywordStatusDesc) {
            this.keywordStatusDesc = keywordStatusDesc;
        }

        public String getKeywordTypeId() {
            return keywordTypeId;
        }

        public void setKeywordTypeId(String keywordTypeId) {
            this.keywordTypeId = keywordTypeId;
        }

        public String getKeywordTypeDesc() {
            return keywordTypeDesc;
        }

        public void setKeywordTypeDesc(String keywordTypeDesc) {
            this.keywordTypeDesc = keywordTypeDesc;
        }

        public int getKeywordStatusToogle() {
            return keywordStatusToogle;
        }

        public void setKeywordStatusToogle(int keywordStatusToogle) {
            this.keywordStatusToogle = keywordStatusToogle;
        }

        public String getKeywordPriceBidFmt() {
            return keywordPriceBidFmt;
        }

        public void setKeywordPriceBidFmt(String keywordPriceBidFmt) {
            this.keywordPriceBidFmt = keywordPriceBidFmt;
        }

        public String getStatAvgClick() {
            return statAvgClick;
        }

        public void setStatAvgClick(String statAvgClick) {
            this.statAvgClick = statAvgClick;
        }

        public String getStatTotalSpent() {
            return statTotalSpent;
        }

        public void setStatTotalSpent(String statTotalSpent) {
            this.statTotalSpent = statTotalSpent;
        }

        public String getStatTotalImpression() {
            return statTotalImpression;
        }

        public void setStatTotalImpression(String statTotalImpression) {
            this.statTotalImpression = statTotalImpression;
        }

        public String getStatTotalClick() {
            return statTotalClick;
        }

        public void setStatTotalClick(String statTotalClick) {
            this.statTotalClick = statTotalClick;
        }

        public String getStatTotalCtr() {
            return statTotalCtr;
        }

        public void setStatTotalCtr(String statTotalCtr) {
            this.statTotalCtr = statTotalCtr;
        }

        public String getStatTotalConversion() {
            return statTotalConversion;
        }

        public void setStatTotalConversion(String statTotalConversion) {
            this.statTotalConversion = statTotalConversion;
        }

        public String getLabelEdit() {
            return labelEdit;
        }

        public void setLabelEdit(String labelEdit) {
            this.labelEdit = labelEdit;
        }

        public String getLabelPerClick() {
            return labelPerClick;
        }

        public void setLabelPerClick(String labelPerClick) {
            this.labelPerClick = labelPerClick;
        }

        public String getLabelOf() {
            return labelOf;
        }

        public void setLabelOf(String labelOf) {
            this.labelOf = labelOf;
        }

    }

    public static class Page {

        @SerializedName("current")
        @Expose
        private int current;
        @SerializedName("per_page")
        @Expose
        private int perPage;
        @SerializedName("min")
        @Expose
        private int min;
        @SerializedName("max")
        @Expose
        private int max;
        @SerializedName("total")
        @Expose
        private int total;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

    }

    public static class Page_ {

        @SerializedName("current")
        @Expose
        private int current;
        @SerializedName("per_page")
        @Expose
        private int perPage;
        @SerializedName("min")
        @Expose
        private int min;
        @SerializedName("max")
        @Expose
        private int max;
        @SerializedName("total")
        @Expose
        private int total;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

    }

}






