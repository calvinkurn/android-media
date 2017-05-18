package com.tokopedia.seller.topads.keyword.view.domain.model;

import java.util.List;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardDomain {
    private Meta meta;
    private Page_ page;
    private List<Datum> data = null;
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
        private Page page;

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

    }

    public static class Error {
        private String code;
        private String title;
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
        private int keywordId;// 1
        private String keywordTag;// 2
        private int groupId;// 3
        private String groupName;// 4
        private int keywordStatus;// 5
        private String keywordStatusDesc;// 6
        private String keywordTypeId;//7
        private String keywordTypeDesc;// 8
        private int keywordStatusToogle;// 9
        private String keywordPriceBidFmt;// 10
        private String statAvgClick; // 11
        private String statTotalSpent; // 12
        private String statTotalImpression; // 13
        private String statTotalClick; // 14
        private String statTotalCtr; // 15
        private String statTotalConversion; // 16
        private String labelEdit; // 17
        private String labelPerClick; // 18
        private String labelOf; // 19

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
        private int current;
        private int perPage;
        private int min;
        private int max;
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
        private int current;
        private int perPage;
        private int min;
        private int max;
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
